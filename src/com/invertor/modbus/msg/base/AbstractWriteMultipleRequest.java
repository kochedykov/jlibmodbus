package com.invertor.modbus.msg.base;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;

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
abstract public class AbstractWriteMultipleRequest extends AbstractMultipleRequest {

    private byte[] values;
    private int byteCount;

    protected AbstractWriteMultipleRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    protected AbstractWriteMultipleRequest(int serverAddress, int startAddress, byte[] values, int quantity) throws ModbusNumberException {
        super(serverAddress, startAddress, quantity);

        setByteCount(values.length);
        setValues(values);
    }

    @Override
    public void writeData(ModbusOutputStream fifo) throws IOException {
        super.writeData(fifo);
        fifo.write(getByteCount());
        fifo.write(getValues());
    }

    @Override
    protected void readData(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        super.readData(fifo);
        setByteCount(fifo.read());
        values = new byte[byteCount];
        int size;
        if ((size = fifo.read(values, 0, getByteCount())) < getByteCount())
            Modbus.log().warning(getByteCount() + " bytes expected, but " + size + " received.");
    }

    protected int getByteCount() {
        return byteCount;
    }

    private void setByteCount(int byteCount) {
        this.byteCount = byteCount;
    }

    public byte[] getValues() {
        return Arrays.copyOf(values, values.length);
    }

    public void setValues(byte[] values) {
        this.values = Arrays.copyOf(values, values.length);
    }

    @Override
    protected int dataSize() {
        return super.dataSize() + 1 + getByteCount();
    }
}
