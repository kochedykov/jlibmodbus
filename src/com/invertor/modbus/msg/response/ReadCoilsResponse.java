package com.invertor.modbus.msg.response;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.AbstractReadResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.utils.DataUtils;
import com.invertor.modbus.utils.ModbusFunctionCode;

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
public class ReadCoilsResponse extends AbstractReadResponse {

    private boolean[] coils;

    public ReadCoilsResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public ReadCoilsResponse(int serverAddress, boolean[] coils) throws ModbusNumberException {
        super(serverAddress, calcByteCount(coils));

        this.coils = coils;
    }

    static public int calcByteCount(boolean[] coils) {
        return calcByteCount(coils.length);
    }

    static public int calcByteCount(int coilCount) {
        return (int) Math.ceil((double) coilCount / 8);
    }

    final public boolean[] getCoils() {
        return coils;
    }

    final public void setCoils(boolean[] coils) {
        this.coils = coils;
        setByteCount(calcByteCount(coils));
    }

    @Override
    final protected void readData(ModbusInputStream fifo) throws IOException {
        byte[] coils = new byte[getByteCount()];
        int size;
        if ((size = fifo.read(coils)) < coils.length)
            Modbus.log().warning(coils.length + " bytes expected, but " + size + " received.");
        this.coils = DataUtils.toBitsArray(coils, coils.length * 8);
    }

    @Override
    final protected void writeData(ModbusOutputStream fifo) throws IOException {
        fifo.write(DataUtils.toByteArray(coils));
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.READ_COILS.toInt();
    }
}
