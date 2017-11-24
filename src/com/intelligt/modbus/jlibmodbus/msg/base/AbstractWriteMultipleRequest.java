package com.intelligt.modbus.jlibmodbus.msg.base;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;

import java.io.IOException;

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

    protected AbstractWriteMultipleRequest() {
        super();
    }

    @Override
    public void writeData(ModbusOutputStream fifo) throws IOException {
        super.writeData(fifo);
        fifo.write(getByteCount());
        fifo.write(getBytes());
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

    public int getByteCount() {
        return byteCount;
    }

    public void setByteCount(int byteCount) throws ModbusNumberException {
        if (byteCount > Modbus.MAX_WRITE_REGISTER_COUNT * 2) {
            //TODO add a description
            throw new ModbusNumberException("" + byteCount);
        }
        this.byteCount = byteCount;
    }

    public byte[] getBytes() {
        return values;
    }

    public void setBytes(byte[] values) throws ModbusNumberException {
        this.values = values;
        setByteCount(values.length);
    }

    @Override
    protected int dataSize() {
        return super.dataSize() + 1 + getByteCount();
    }
}
