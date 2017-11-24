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

public abstract class AbstractWriteResponse extends ModbusResponse {
    private int startAddress = 0;

    protected AbstractWriteResponse() {
        super();
    }

    @Override
    final protected void readResponse(ModbusInputStream fifo) throws IOException {
        try {
            setStartAddress(fifo.readShortBE());
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        }
        readValue(fifo);
    }

    @Override
    final public void writeResponse(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getStartAddress());
        writeValue(fifo);
    }

    abstract protected void readValue(ModbusInputStream fifo) throws IOException;

    abstract protected void writeValue(ModbusOutputStream fifo) throws IOException;

    final public int getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(int startAddress) throws ModbusNumberException {
        if (!(Modbus.checkStartAddress(startAddress)))
            throw new ModbusNumberException("Error in start address", startAddress);
        this.startAddress = startAddress;
    }

    @Override
    final protected int responseSize() {
        return 4;
    }
}
