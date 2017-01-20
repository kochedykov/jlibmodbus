package com.invertor.modbus.msg.request;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.AbstractMultipleRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.ReadCoilsResponse;
import com.invertor.modbus.utils.ModbusFunctionCode;

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

    public ReadCoilsRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public ReadCoilsRequest(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        super(serverAddress, startAddress, quantity);
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
        ReadCoilsResponse response = new ReadCoilsResponse(getServerAddress());
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
