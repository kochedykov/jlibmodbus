package com.sbpinvertor.modbus.data.response;

import com.sbpinvertor.modbus.ModbusFunction;
import com.sbpinvertor.modbus.data.ModbusInputStream;
import com.sbpinvertor.modbus.data.ModbusOutputStream;
import com.sbpinvertor.modbus.data.base.AbstractReadResponse;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.utils.DataUtils;

import java.io.IOException;

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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ReadCoilsResponse extends AbstractReadResponse {

    private boolean[] coils;

    public ReadCoilsResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    ReadCoilsResponse(int serverAddress, boolean[] coils) throws ModbusNumberException {
        super(serverAddress, (int) Math.ceil(coils.length / 8));

        this.coils = coils;
    }

    final public boolean[] getCoils() {
        return coils;
    }

    @Override
    final protected void readData(ModbusInputStream fifo) throws IOException {
        byte[] coils = new byte[getByteCount()];
        fifo.read(coils, 0, getByteCount());
        this.coils = DataUtils.toBitsArray(coils, getByteCount() * 8);
    }

    @Override
    final protected void writeData(ModbusOutputStream fifo) throws IOException {
        fifo.write(DataUtils.toByteArray(coils));
    }

    @Override
    public ModbusFunction getFunction() {
        return ModbusFunction.READ_COILS;
    }
}
