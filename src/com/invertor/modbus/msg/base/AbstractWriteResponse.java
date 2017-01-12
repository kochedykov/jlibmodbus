package com.invertor.modbus.msg.base;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;

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

    protected AbstractWriteResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    protected AbstractWriteResponse(int serverAddress, int startAddress) throws ModbusNumberException {
        super(serverAddress);

        if (!(Modbus.checkStartAddress(startAddress)))
            throw new ModbusNumberException("Error in start address", startAddress);

        setStartAddress(startAddress);
    }

    @Override
    final protected void readResponse(ModbusInputStream fifo) throws IOException {
        setStartAddress(fifo.readShortBE());
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

    private void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    @Override
    final protected int responseSize() {
        return 4;
    }
}
