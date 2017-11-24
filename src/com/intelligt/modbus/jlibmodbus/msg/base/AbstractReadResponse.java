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
abstract public class AbstractReadResponse extends ModbusResponse {

    private int byteCount = 0;

    protected AbstractReadResponse() {
        super();
    }

    public int getByteCount() {
        return byteCount;
    }

    protected void setByteCount(int byteCount) throws ModbusNumberException {
        if (byteCount > (Modbus.MAX_PDU_LENGTH - 2))
            throw new ModbusNumberException("Byte count greater than max allowable");
        this.byteCount = byteCount;
    }

    @Override
    final public void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        setByteCount(fifo.read());
        readData(fifo);
    }

    @Override
    final public void writeResponse(ModbusOutputStream fifo) throws IOException {
        fifo.write(getByteCount());
        writeData(fifo);
    }

    abstract protected void readData(ModbusInputStream fifo) throws IOException, ModbusNumberException;

    abstract protected void writeData(ModbusOutputStream fifo) throws IOException;

    @Override
    final protected int responseSize() {
        return 1 + getByteCount();
    }
}
