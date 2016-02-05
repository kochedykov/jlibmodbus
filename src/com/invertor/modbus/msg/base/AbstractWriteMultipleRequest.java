package com.invertor.modbus.msg.base;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;

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
abstract public class AbstractWriteMultipleRequest extends AbstractMultipleRequest {

    private byte[] values;
    private int byteCount;

    protected AbstractWriteMultipleRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    protected AbstractWriteMultipleRequest(int serverAddress, int startAddress, byte[] values, int quantity) throws ModbusNumberException {
        super(serverAddress, startAddress, quantity);

        setByteCount(values.length);
        setValues(values);
    }

    @Override
    public void writeData(ModbusOutputStream fifo) throws IOException {
        super.writeData(fifo);
        fifo.write(getByteCount());
        fifo.write(getValues());
    }

    @Override
    protected void readData(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        super.readData(fifo);
        setByteCount(fifo.read());
        setValues(new byte[byteCount]);
        int size;
        if ((size = fifo.read(getValues(), 0, getByteCount())) < getByteCount())
            Modbus.log().warning(getByteCount() + " bytes expected, but " + size + " received.");
    }

    protected int getByteCount() {
        return byteCount;
    }

    private void setByteCount(int byteCount) {
        this.byteCount = byteCount;
    }

    public byte[] getValues() {
        return values;
    }

    public void setValues(byte[] values) {
        this.values = values;
    }

    @Override
    protected int dataSize() {
        return super.dataSize() + 1 + getByteCount();
    }
}
