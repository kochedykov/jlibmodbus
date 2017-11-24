package com.intelligt.modbus.jlibmodbus.msg.request;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.WriteSingleCoilResponse;
import com.intelligt.modbus.jlibmodbus.utils.ModbusFunctionCode;

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
final public class WriteSingleCoilRequest extends WriteSingleRegisterRequest {

    public WriteSingleCoilRequest() {
        super();
    }

    @Override
    protected Class getResponseClass() {
        return WriteSingleCoilResponse.class;
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        WriteSingleCoilResponse response = (WriteSingleCoilResponse) getResponse();
        response.setStartAddress(getStartAddress());
        response.setValue(getValue());
        try {
            dataHolder.writeCoil(getStartAddress(), getCoil());
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof WriteSingleCoilResponse)) {
            return false;
        }
        WriteSingleCoilResponse r = (WriteSingleCoilResponse) response;
        return r.getStartAddress() == getStartAddress() && r.getValue() == getValue();
    }

    public boolean getCoil() {
        return Modbus.toCoil(getValue());
    }

    public void setCoil(boolean coil) throws ModbusNumberException {
        setValue(Modbus.fromCoil(coil));
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.WRITE_SINGLE_COIL.toInt();
    }
}
