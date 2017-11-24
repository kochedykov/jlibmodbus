package com.intelligt.modbus.jlibmodbus.msg.request;

import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadInputRegistersResponse;
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

final public class ReadInputRegistersRequest extends ReadHoldingRegistersRequest {

    public ReadInputRegistersRequest() {
        super();
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        ReadInputRegistersResponse response = (ReadInputRegistersResponse) getResponse();
        response.setServerAddress(getServerAddress());
        try {
            int[] range = dataHolder.readInputRegisterRange(getStartAddress(), getQuantity());
            response.setBuffer(range);
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof ReadInputRegistersResponse)) {
            return false;
        }
        ReadInputRegistersResponse r = (ReadInputRegistersResponse) response;
        return r.getByteCount() == getQuantity() * 2;
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.READ_INPUT_REGISTERS.toInt();
    }

    @Override
    protected Class getResponseClass() {
        return ReadInputRegistersResponse.class;
    }
}
