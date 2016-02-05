package com.sbpinvertor.modbus.msg.base;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.net.stream.base.ModbusOutputStream;

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

    protected AbstractWriteResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    protected AbstractWriteResponse(int serverAddress, int startAddress) throws ModbusNumberException {
        super(serverAddress);

        if (!(Modbus.checkStartAddress(startAddress)))
            throw new ModbusNumberException("Error in start address", startAddress);

        setStartAddress(startAddress);
    }

    @Override
    final protected void readResponse(ModbusInputStream fifo) throws IOException {
        setStartAddress(fifo.readShortBE());
        readValue(fifo);
    }

    @Override
    final public void writeResponse(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getStartAddress());
        writeValue(fifo);
    }

    abstract protected void readValue(ModbusInputStream fifo) throws IOException;

    abstract protected void writeValue(ModbusOutputStream fifo) throws IOException;

    final public int getStartAddress() {
        return startAddress;
    }

    private void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    @Override
    final protected int responseSize() {
        return 4;
    }
}
