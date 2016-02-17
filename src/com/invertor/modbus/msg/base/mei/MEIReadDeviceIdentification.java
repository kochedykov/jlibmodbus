package com.invertor.modbus.msg.base.mei;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.data.mei.ReadDeviceIdentificationInterface;
import com.invertor.modbus.data.mei.ReadDeviceIdentificationInterface.DataObject;
import com.invertor.modbus.exception.IllegalDataAddressException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.utils.MEITypeCode;

import java.io.IOException;

/**
 * Copyright (c) 2015-2016 JSC Invertor
 * [http://www.sbp-invertor.ru]
 * <p/>
 * This file is part of JLibModbus.
 * <p/>
 * JLibModbus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
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
        setReadDeviceIdCode(ReadDeviceIdentificationCode.get(fifo.read()));
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
        for (int i = 0; i < getNumberOfObjects(); i++) {
            DataObject o = getObjects()[i];
            fifo.write(o.getId());
            fifo.write(o.getValue().length);
            fifo.write(o.getValue());
        }
    }

    @Override
    public void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        setReadDeviceIdCode(ReadDeviceIdentificationCode.get(fifo.read()));
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
            getObjects()[i] = new DataObject(id, value);
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
    public void process(DataHolder dataHolder) throws IllegalDataAddressException {
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
        int size = 0;
        int number_of_objects = 0;
        final int list_of_objects_max = (Modbus.MAX_PDU_LENGTH - SIZE_OF_HEADER);
        for (DataObject object : getObjects()) {
            size += object.getValue().length + 2;//object id + object length = 2
            if (size < list_of_objects_max) {
                ++number_of_objects;
            }
        }
        setResponseSize(size + SIZE_OF_HEADER);
        if (getObjects().length == number_of_objects) {
            setMoreFollows(false);
        } else {
            setNextObjectId(number_of_objects + 1);
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

    public void setReadDeviceIdCode(ReadDeviceIdentificationCode readDeviceIdCode) {
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
        return objects;
    }

    public void setObjects(DataObject[] objects) {
        this.objects = objects;
    }
}
