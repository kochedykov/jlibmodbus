package com.invertor.modbus.msg.response;

import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;

import java.io.IOException;

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
public class IllegalFunctionResponse extends ModbusResponse {
    private int function;

    public IllegalFunctionResponse(int serverAddress, int function) throws ModbusNumberException {
        super(serverAddress);
        this.function = function;
        setException();
    }

    @Override
    public int getFunction() {
        return function;
    }

    @Override
    protected void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        //no op
    }

    @Override
    protected void writeResponse(ModbusOutputStream fifo) throws IOException {
        //no op
    }

    @Override
    protected int responseSize() {
        return 0;
    }
}
