package com.sbpinvertor.modbus.msg.response;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
final public class WriteSingleCoilResponse extends WriteSingleRegisterResponse {

    public WriteSingleCoilResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public WriteSingleCoilResponse(int serverAddress, int startAddress, boolean coil) throws ModbusNumberException {
        super(serverAddress, startAddress, Modbus.fromCoil(coil));
    }

    @Override
    protected boolean checkValue() {
        return getValue() == Modbus.COIL_VALUE_ON || getValue() == Modbus.COIL_VALUE_OFF;
    }

    @Override
    public ModbusFunctionCode getFunction() {
        return ModbusFunctionCode.WRITE_SINGLE_COIL;
    }
}
