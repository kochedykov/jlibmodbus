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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
abstract public class AbstractWriteMultipleRequest extends AbstractMultipleRequest {

    private byte[] values;
    private int byteCount;

    public AbstractWriteMultipleRequest(int serverAddress, int startAddress, byte[] values, int quantity) throws ModbusNumberException {
        super(serverAddress, startAddress, quantity);

        setByteCount(values.length);
        setValues(values);
    }

    public AbstractWriteMultipleRequest(AbstractMultipleRequest msg) {
        super(msg);
    }

    @Override
    public void writeData(ByteFifo fifo) throws ModbusDataException {
        super.writeData(fifo);
        fifo.write(getByteCount());
        fifo.write(getValues());
    }

    @Override
    public void readPDU(ByteFifo fifo) throws ModbusDataException {
        super.readPDU(fifo);
        setByteCount(fifo.read());
        setValues(new byte[byteCount]);
        fifo.read(getValues());
    }

    public int getByteCount() {
        return byteCount;
    }

    public void setByteCount(int byteCount) {
        this.byteCount = byteCount;
    }

    public byte[] getValues() {
        return values;
    }

    public void setValues(byte[] values) {
        this.values = values;
    }
}
