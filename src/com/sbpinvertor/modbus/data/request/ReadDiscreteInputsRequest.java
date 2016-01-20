package com.sbpinvertor.modbus.data.request;

import com.sbpinvertor.modbus.ModbusFunction;
import com.sbpinvertor.modbus.exception.ModbusNumberException;

/**
 * Copyright (c) 2015-2016 JSC "Zavod "Invertor"
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

public class ReadDiscreteInputsRequest extends ReadCoilsRequest {

    public ReadDiscreteInputsRequest(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        super(serverAddress, startAddress, quantity);
    }

    @Override
    public ModbusFunction getFunction() {
        return ModbusFunction.READ_DISCRETE_INPUTS;
    }
}
