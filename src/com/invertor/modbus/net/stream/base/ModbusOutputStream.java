package com.invertor.modbus.net.stream.base;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.utils.ByteFifo;
import com.invertor.modbus.utils.DataUtils;

import java.io.IOException;
import java.io.OutputStream;

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
abstract public class ModbusOutputStream extends OutputStream {

    private final ByteFifo fifo = new ByteFifo(Modbus.MAX_RTU_ADU_LENGTH);

    @Override
    public void write(byte[] b) throws IOException {
        fifo.write(b);
    }

    @Override
    public void write(int b) throws IOException {
        fifo.write(b);
    }

    /**
     * it should have invoked last
     */
    @Override
    public void flush() throws IOException {
        fifo.reset();
    }

    public void reset() {

    }

    public void writeShortBE(int s) throws IOException {
        write(DataUtils.byteHigh(s));
        write(DataUtils.byteLow(s));
    }

    public void writeShortLE(int s) throws IOException {
        write(DataUtils.byteLow(s));
        write(DataUtils.byteHigh(s));
    }

    public byte[] toByteArray() {
        return fifo.toByteArray();
    }
}
