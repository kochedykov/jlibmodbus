package com.sbpinvertor.modbus.data.base;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.ModbusFunction;
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

abstract public class ModbusMessage {

    private final int serverAddress;

    public ModbusMessage(int serverAddress) throws ModbusNumberException {
        if (!Modbus.checkServerAddress(serverAddress))
            throw new ModbusNumberException("Error in slave id", serverAddress);
        this.serverAddress = serverAddress;
    }

    public ModbusMessage(ModbusMessage msg) {
        this.serverAddress = msg.serverAddress;
    }

    final public void write(ModbusOutputStream fifo) throws IOException {
        fifo.write(getServerAddress());
        writePDU(fifo);
    }

    final public void read(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        readPDU(fifo);
    }

    abstract protected void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException;

    abstract protected void writePDU(ModbusOutputStream fifo) throws IOException;

    abstract public ModbusFunction getFunction();

    public int size() {
        return 1 + pduSize();
    }

    abstract protected int pduSize();

    public int getServerAddress() {
        return serverAddress;
    }
}
