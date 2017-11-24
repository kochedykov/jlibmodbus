package com.intelligt.modbus.jlibmodbus.msg.response;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.base.AbstractWriteResponse;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
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
public class WriteSingleRegisterResponse extends AbstractWriteResponse {

    int value;

    public WriteSingleRegisterResponse() {
        super();
    }

    @Override
    final protected void readValue(ModbusInputStream fifo) throws IOException {
        try {
            setValue(fifo.readShortBE());
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        }
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

    final public void setValue(int value) throws ModbusNumberException {
        this.value = ((short) value) & 0xffff;
        if (!checkValue())
            throw new ModbusNumberException("Error in register value", value);
    }
}
