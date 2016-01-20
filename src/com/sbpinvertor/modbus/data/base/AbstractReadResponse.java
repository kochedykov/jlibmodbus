package com.sbpinvertor.modbus.data.base;

import com.sbpinvertor.modbus.exception.ModbusDataException;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.utils.ByteFifo;

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
abstract public class AbstractReadResponse extends ModbusResponse {

    private int byteCount = 0;

    public AbstractReadResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public AbstractReadResponse(int serverAddress, int byteCount) throws ModbusNumberException {
        super(serverAddress);

        this.byteCount = byteCount;
    }

    public int getByteCount() {
        return byteCount;
    }

    private void setByteCount(int byteCount) {
        this.byteCount = byteCount;
    }

    @Override
    final public void readResponse(ByteFifo fifo) throws ModbusDataException {
        setByteCount(fifo.read());
        readData(fifo);
    }

    @Override
    final public void writeResponse(ByteFifo fifo) throws ModbusDataException {
        fifo.write(getByteCount());
        writeData(fifo);
    }

    abstract protected void readData(ByteFifo fifo) throws ModbusDataException;

    abstract protected void writeData(ByteFifo fifo) throws ModbusDataException;
}
