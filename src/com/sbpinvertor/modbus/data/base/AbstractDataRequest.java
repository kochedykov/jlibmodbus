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
abstract public class AbstractDataRequest extends ModbusRequest {

    private int startAddress;

    public AbstractDataRequest(int serverAddress, int startAddress) throws ModbusNumberException {
        super(serverAddress);

        if (!Modbus.checkStartAddress(startAddress))
            throw new ModbusNumberException("Error in start address", startAddress);

        setStartAddress(startAddress);
    }

    public AbstractDataRequest(AbstractDataRequest msg) {
        super(msg);

        setStartAddress(msg.startAddress);
    }

    abstract protected void writeData(ByteFifo fifo) throws ModbusDataException;

    @Override
    protected void writeRequest(ByteFifo fifo) throws ModbusDataException {
        fifo.writeShortBE(getStartAddress());
        writeData(fifo);
    }

    public int getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }
}
