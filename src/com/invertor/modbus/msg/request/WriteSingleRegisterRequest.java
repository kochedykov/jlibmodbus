package com.invertor.modbus.msg.request;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.AbstractDataRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.WriteSingleRegisterResponse;
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
public class WriteSingleRegisterRequest extends AbstractDataRequest {

    private int value;

    public WriteSingleRegisterRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public WriteSingleRegisterRequest(int serverAddress, int startAddress, int value) throws ModbusNumberException {
        super(serverAddress, startAddress);

        if (!Modbus.checkRegisterValue(value)) {
            throw new ModbusNumberException("Register value out of range", value);
        }
        setValue(value);
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        WriteSingleRegisterResponse response = new WriteSingleRegisterResponse(getServerAddress(), getStartAddress(), getValue());
        try {
            dataHolder.writeHoldingRegister(getStartAddress(), getValue());
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof WriteSingleRegisterResponse)) {
            return false;
        }
        WriteSingleRegisterResponse r = (WriteSingleRegisterResponse) response;
        return r.getStartAddress() == getStartAddress() && r.getValue() == getValue();
    }

    @Override
    final protected void readData(ModbusInputStream fifo) throws IOException {
        setValue(fifo.readShortBE());
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.WRITE_SINGLE_REGISTER.toInt();
    }

    @Override
    final public void writeData(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getValue());
    }

    @Override
    protected int dataSize() {
        return 2;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = ((short) value) & 0xffff;
    }
}
