package com.intelligt.modbus.jlibmodbus.msg.base.mei;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.data.mei.ReadDeviceIdentificationInterface;
import com.intelligt.modbus.jlibmodbus.data.mei.ReadDeviceIdentificationInterface.DataObject;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.MEITypeCode;

import java.io.IOException;
import java.util.Arrays;

/*
 * Copyright (C) 2016 "Invertor" Factory", JSC
 * [http://www.sbp-invertor.ru]
 *
 * This file is part of JLibModbus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class MEIReadDeviceIdentification implements ModbusEncapsulatedInterface {

    public static final int SIZE_OF_HEADER = 8;
    private int objectId = 0;
    private ReadDeviceIdentificationCode readDeviceIdCode = ReadDeviceIdentificationCode.BASIC_STREAM_ACCESS;
    private ConformityLevel conformityLevel = ConformityLevel.BASIC_STREAM_ONLY;
    private boolean moreFollows = false;
    private int nextObjectId = 0;
    private int numberOfObjects = 0;
    private DataObject[] objects = new DataObject[0];
    private int responseSize = 0;

    private int firstObjectIndex = 0;

    @Override
    public MEITypeCode getTypeCode() {
        return MEITypeCode.READ_DEVICE_IDENTIFICATION;
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        fifo.write(getReadDeviceIdCode().toInt());
        fifo.write(getObjectId());
    }

    @Override
    public void readRequest(ModbusInputStream fifo) throws IOException {
        setReadDeviceId(ReadDeviceIdentificationCode.get(fifo.read()));
        setObjectId(fifo.read());
    }

    @Override
    public int getRequestSize() {
        return 2;
    }

    @Override
    public void writeResponse(ModbusOutputStream fifo) throws IOException {
        fifo.write(getReadDeviceIdCode().toInt());
        fifo.write(getConformityLevel().toInt());
        fifo.write(isMoreFollows() ? 0xFF : 0x00);
        fifo.write(getNextObjectId());
        fifo.write(getNumberOfObjects());
        for (int i = getFirstObjectIndex(); i < getFirstObjectIndex() + getNumberOfObjects(); i++) {
            DataObject o = objects[i];
            fifo.write(o.getId());
            fifo.write(o.getValue().length);
            fifo.write(o.getValue());
        }
    }

    @Override
    public void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        setReadDeviceId(ReadDeviceIdentificationCode.get(fifo.read()));
        setConformityLevel(ConformityLevel.get(fifo.read()));
        setMoreFollows(fifo.read() == 0xff);
        setNextObjectId(fifo.read());
        setNumberOfObjects(fifo.read());
        if (getNumberOfObjects() > 0x7F) {
            throw new ModbusNumberException("Illegal number of data objects", getNumberOfObjects());
        }
        setObjects(new DataObject[getNumberOfObjects()]);
        int size = SIZE_OF_HEADER;
        for (int i = 0; i < getNumberOfObjects(); i++) {
            int id = fifo.read();
            int length = fifo.read();
            size += 2 + length;
            if (size > Modbus.MAX_PDU_LENGTH)
                throw new ModbusNumberException("Exceeded max pdu length", size);
            byte[] value = new byte[length];
            int read;
            if ((read = fifo.read(value)) != length)
                Modbus.log().warning(length + " bytes expected, but " + read + " received.");
            objects[i] = new DataObject(id, value);
        }
        setResponseSize(size);
    }

    @Override
    public int getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(int responseSize) {
        this.responseSize = responseSize;
    }

    @Override
    public void process(DataHolder dataHolder) {
        ReadDeviceIdentificationInterface deviceId = dataHolder.getReadDeviceIdentificationInterface();
        if (deviceId.getExtended().length != 0) {
            setConformityLevel(ConformityLevel.EXTENDED_STREAM_AND_INDIVIDUAL);
        } else if (deviceId.getRegular().length != 0) {
            setConformityLevel(ConformityLevel.REGULAR_STREAM_AND_INDIVIDUAL);
        } else {
            setConformityLevel(ConformityLevel.BASIC_STREAM_AND_INDIVIDUAL);
        }
        switch (getReadDeviceIdCode()) {
            case BASIC_STREAM_ACCESS:
                setObjects(deviceId.getBasic());
                break;
            case REGULAR_STREAM_ACCESS:
                setObjects(deviceId.getRegular());
                break;
            case EXTENDED_STREAM_ACCESS:
                setObjects(deviceId.getExtended());
                break;
            case ONE_SPECIFIC_INDIVIDUAL_ACCESS:
            default:
                setObjects(new DataObject[]{deviceId.getValue(getObjectId())});
        }
        int size = SIZE_OF_HEADER;
        int number_of_objects = 0;

        if (getObjectId() != 0) {
            for (int i = 0; i < objects.length && getFirstObjectIndex() == 0; i++) {
                if (objects[i].getId() == getObjectId()) {
                    setFirstObjectIndex(i);
                }
            }
        }

        for (int i = getFirstObjectIndex(); i < objects.length; i++) {
            size += objects[i].getValue().length + 2;//object id + object length = 2
            if (size >= Modbus.MAX_PDU_LENGTH) {
                break;
            }
            number_of_objects++;
        }
        setResponseSize(size);
        if ((objects.length - getFirstObjectIndex()) > number_of_objects) {
            setNextObjectId(number_of_objects + 1);
        } else {
            setMoreFollows(false);
        }
        setNumberOfObjects(number_of_objects);
    }

    public int getNumberOfObjects() {
        return numberOfObjects;
    }

    public void setNumberOfObjects(int numberOfObjects) {
        this.numberOfObjects = numberOfObjects;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public ReadDeviceIdentificationCode getReadDeviceIdCode() {
        return readDeviceIdCode;
    }

    public void setReadDeviceId(ReadDeviceIdentificationCode readDeviceIdCode) {
        this.readDeviceIdCode = readDeviceIdCode;
    }

    public ConformityLevel getConformityLevel() {
        return conformityLevel;
    }

    public void setConformityLevel(ConformityLevel conformityLevel) {
        this.conformityLevel = conformityLevel;
    }

    public boolean isMoreFollows() {
        return moreFollows;
    }

    public void setMoreFollows(boolean moreFollows) {
        this.moreFollows = moreFollows;
    }

    public int getNextObjectId() {
        return nextObjectId;
    }

    public void setNextObjectId(int nextObjectId) {
        this.nextObjectId = nextObjectId;
    }

    public DataObject[] getObjects() {
        return Arrays.copyOf(objects, objects.length);
    }

    public void setObjects(DataObject[] objects) {
        this.objects = Arrays.copyOf(objects, objects.length);
    }

    public int getFirstObjectIndex() {
        return firstObjectIndex;
    }

    public void setFirstObjectIndex(int firstObjectIndex) {
        this.firstObjectIndex = firstObjectIndex;
    }
}
