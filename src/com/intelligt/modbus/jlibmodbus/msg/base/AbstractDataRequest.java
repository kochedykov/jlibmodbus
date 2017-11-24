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
abstract public class AbstractDataRequest extends ModbusRequest {

    private int startAddress;

    protected AbstractDataRequest() {
        super();
    }

    abstract protected void writeData(ModbusOutputStream fifo) throws IOException;

    abstract protected void readData(ModbusInputStream fifo) throws IOException, ModbusNumberException;

    @Override
    final public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        setStartAddress(fifo.readShortBE());
        readData(fifo);
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getStartAddress());
        writeData(fifo);
    }

    public int getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(int startAddress) throws ModbusNumberException {
        if (!Modbus.checkStartAddress(startAddress)) {
            throw new ModbusNumberException("Error in start address", startAddress);
        }
        this.startAddress = startAddress;
    }

    @Override
    final public int requestSize() {
        return 2 + dataSize();
    }

    abstract protected int dataSize();
}
