package com.intelligt.modbus.jlibmodbus.msg.request;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.msg.base.AbstractMultipleRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadCoilsResponse;
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

public class ReadCoilsRequest extends AbstractMultipleRequest {

    public ReadCoilsRequest() {
        super();
    }

    @Override
    protected Class getResponseClass() {
        return ReadCoilsResponse.class;
    }

    @Override
    final public boolean checkAddressRange(int startAddress, int quantity) {
        return Modbus.checkReadCoilCount(quantity) &&
                Modbus.checkStartAddress(startAddress) &&
                /*
                 * registers quantity equals to rounded value of coils quantity/16
                 */
                Modbus.checkEndAddress(startAddress + (int) Math.ceil((double) quantity / 16));
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.READ_COILS.toInt();
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        ReadCoilsResponse response = (ReadCoilsResponse) getResponse();
        try {
            boolean[] range = dataHolder.readCoilRange(getStartAddress(), getQuantity());
            response.setCoils(range);
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof ReadCoilsResponse)) {
            return false;
        }
        ReadCoilsResponse r = (ReadCoilsResponse) response;
        return (r.getByteCount() == ReadCoilsResponse.calcByteCount(getQuantity()));
    }
}
