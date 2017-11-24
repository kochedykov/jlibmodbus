package com.intelligt.modbus.jlibmodbus.msg.base;

import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.ModbusExceptionCode;
import com.intelligt.modbus.jlibmodbus.utils.ModbusFunctionCode;

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

public abstract class ModbusResponse extends ModbusMessage {

    private volatile ModbusExceptionCode modbusExceptionCode = ModbusExceptionCode.NO_EXCEPTION;

    protected ModbusResponse() {
        super();
    }

    final public boolean isException() {
        return getModbusExceptionCode() != ModbusExceptionCode.NO_EXCEPTION;
    }

    final public void setException() {
        modbusExceptionCode = ModbusExceptionCode.UNKNOWN_EXCEPTION;
    }

    @Override
    final protected void writePDU(ModbusOutputStream fifo) throws IOException {
        if (isException()) {
            fifo.write(ModbusFunctionCode.getExceptionValue(getFunction()));
            fifo.write(getModbusExceptionCode().getValue());
        } else {
            fifo.write(getFunction());
            writeResponse(fifo);
        }
    }

    @Override
    final public void readPDU(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        if (isException()) {
            setModbusExceptionCode(fifo.read());
        } else {
            readResponse(fifo);
        }
    }

    abstract protected void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException;

    abstract protected void writeResponse(ModbusOutputStream fifo) throws IOException;

    final public ModbusExceptionCode getModbusExceptionCode() {
        return modbusExceptionCode;
    }

    final public void setModbusExceptionCode(int code) {
        modbusExceptionCode = ModbusExceptionCode.get(code);
    }

    @Override
    final protected int pduSize() {
        return 1 + responseSize();
    }

    abstract protected int responseSize();
}
