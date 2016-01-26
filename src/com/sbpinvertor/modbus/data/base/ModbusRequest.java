package com.sbpinvertor.modbus.data.base;

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
abstract public class ModbusRequest extends ModbusMessage {

    public ModbusRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public ModbusRequest(ModbusMessage msg) {
        super(msg);
    }

    abstract protected void writeRequest(ModbusOutputStream fifo) throws IOException;

    @Override
    final public void writePDU(ModbusOutputStream fifo) throws IOException {
        fifo.write(getFunction().getCode());
        writeRequest(fifo);
    }

    @Override
    final protected int pduSize() {
        return 1 + requestSize();
    }

    abstract protected int requestSize();
}
