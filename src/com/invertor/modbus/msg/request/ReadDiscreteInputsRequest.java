package com.invertor.modbus.msg.request;

import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.ReadCoilsResponse;
import com.invertor.modbus.msg.response.ReadDiscreteInputsResponse;
import com.invertor.modbus.utils.ModbusFunctionCode;

/**
 * Copyright (c) 2015-2016 JSC Invertor
 * [http://www.sbp-invertor.ru]
 * <p/>
 * This file is part of JLibModbus.
 * <p/>
 * JLibModbus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

final public class ReadDiscreteInputsRequest extends ReadCoilsRequest {

    public ReadDiscreteInputsRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public ReadDiscreteInputsRequest(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        super(serverAddress, startAddress, quantity);
    }

    @Override
    public ModbusResponse getResponse(DataHolder dataHolder) throws ModbusNumberException {
        ReadDiscreteInputsResponse response = new ReadDiscreteInputsResponse(getServerAddress());
        try {
            boolean[] range = dataHolder.readDiscreteInputRange(getStartAddress(), getQuantity());
            response.setCoils(range);
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.READ_DISCRETE_INPUTS.toInt();
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        ReadDiscreteInputsResponse r = (ReadDiscreteInputsResponse) response;
        return (r.getByteCount() == ReadCoilsResponse.calcByteCount(getQuantity()));
    }
}
