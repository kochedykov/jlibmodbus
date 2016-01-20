package com.sbpinvertor.modbus.data.base;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.exception.ModbusDataException;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.utils.ByteFifo;

/**
 * Copyright (c) 2015 JSC "Zavod "Invertor"
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

    public AbstractWriteResponse(int serverAddress, int startAddress, int value) throws ModbusNumberException {
        super(serverAddress);

        if (!(Modbus.checkStartAddress(startAddress) && checkValue()))
            throw new ModbusNumberException("Error in start address", startAddress);

        setStartAddress(startAddress);
        setValue(value);
    }

    public AbstractWriteResponse(ModbusMessage msg) {
        super(msg);
    }

    @Override
    protected void readResponse(ByteFifo fifo) throws ModbusDataException {
        startAddress = fifo.readShortBE();
        value = fifo.readShortBE();
    }

    @Override
    public void writeResponse(ByteFifo fifo) throws ModbusDataException {
        fifo.writeShortBE(startAddress);
        fifo.writeShortBE(value);
    }

    abstract protected boolean checkValue();

    public int getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
