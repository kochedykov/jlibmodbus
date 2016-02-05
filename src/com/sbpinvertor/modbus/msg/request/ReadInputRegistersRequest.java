package com.sbpinvertor.modbus.msg.request;

import com.sbpinvertor.modbus.data.DataHolder;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusProtocolException;
import com.sbpinvertor.modbus.msg.base.ModbusResponse;
import com.sbpinvertor.modbus.msg.response.ReadInputRegistersResponse;
import com.sbpinvertor.modbus.utils.ModbusFunctionCode;

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

final public class ReadInputRegistersRequest extends ReadHoldingRegistersRequest {

    public ReadInputRegistersRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public ReadInputRegistersRequest(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        super(serverAddress, startAddress, quantity);
    }

    @Override
    public ModbusResponse getResponse(DataHolder dataHolder) throws ModbusNumberException {
        ReadInputRegistersResponse response = new ReadInputRegistersResponse(getServerAddress());
        try {
            int[] range = dataHolder.readInputRegisterRange(getStartAddress(), getQuantity());
            response.setRegisters(range);
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public ModbusFunctionCode getFunction() {
        return ModbusFunctionCode.READ_INPUT_REGISTERS;
    }
}
