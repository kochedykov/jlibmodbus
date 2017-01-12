package com.invertor.modbus.msg.response;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.AbstractWriteResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.utils.ModbusFunctionCode;

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
public class WriteSingleRegisterResponse extends AbstractWriteResponse {

    int value;

    public WriteSingleRegisterResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public WriteSingleRegisterResponse(int serverAddress, int startAddress, int value) throws ModbusNumberException {
        super(serverAddress, startAddress);
        setValue(value);
        if (!checkValue())
            throw new ModbusNumberException("Error in register value", startAddress);
    }

    @Override
    final protected void readValue(ModbusInputStream fifo) throws IOException {
        setValue(fifo.readShortBE());
    }

    @Override
    final protected void writeValue(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getValue());
    }

    protected boolean checkValue() {
        return Modbus.checkRegisterValue(getValue());
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.WRITE_SINGLE_REGISTER.toInt();
    }

    final public int getValue() {
        return value;
    }

    final public void setValue(int value) {
        this.value = ((short) value) & 0xffff;
    }
}
