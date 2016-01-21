package com.sbpinvertor.modbus.data.base;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.data.ModbusInputStream;
import com.sbpinvertor.modbus.data.ModbusOutputStream;
import com.sbpinvertor.modbus.exception.ModbusNumberException;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

public abstract class AbstractWriteResponse extends ModbusResponse {
    private int startAddress = 0;
    private int value = 0;

    protected AbstractWriteResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    protected AbstractWriteResponse(int serverAddress, int startAddress, int value) throws ModbusNumberException {
        super(serverAddress);

        if (!(Modbus.checkStartAddress(startAddress) && checkValue()))
            throw new ModbusNumberException("Error in start address", startAddress);

        setStartAddress(startAddress);
        setValue(value);
    }

    protected AbstractWriteResponse(ModbusMessage msg) {
        super(msg);
    }

    @Override
    final protected void readResponse(ModbusInputStream fifo) throws IOException {
        setStartAddress(fifo.readShortBE());
        setValue(fifo.readShortBE());
    }

    @Override
    final public void writeResponse(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getStartAddress());
        fifo.writeShortBE(getValue());
    }

    abstract protected boolean checkValue();

    final public int getStartAddress() {
        return startAddress;
    }

    private void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    final protected int getValue() {
        return value;
    }

    final public void setValue(int value) {
        this.value = value;
    }

    @Override
    final protected int responseSize() {
        return 4;
    }
}
