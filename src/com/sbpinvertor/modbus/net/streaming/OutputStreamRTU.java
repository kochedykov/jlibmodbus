package com.sbpinvertor.modbus.net.streaming;

import com.sbpinvertor.conn.SerialPort;
import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.data.ModbusOutputStream;
import com.sbpinvertor.modbus.utils.ByteFifo;
import com.sbpinvertor.utils.CRC16;

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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class OutputStreamRTU extends ModbusOutputStream {

    final private SerialPort serial;
    private final ByteFifo fifo = new ByteFifo(Modbus.MAX_RTU_ADU_LENGTH);
    private int crc;

    public OutputStreamRTU(SerialPort serial) {
        this.serial = serial;
        reset();
    }

    @Override
    public void write(byte[] b) throws IOException {
        fifo.write(b);
        crc = CRC16.calc(crc, b, 0, b.length);
    }

    @Override
    public void write(int b) throws IOException {
        fifo.write(b);
        crc = CRC16.calc(crc, (byte) (b & 0xff));
    }

    @Override
    public void flush() throws IOException {
        writeShortLE(crc);
        serial.write(fifo.toByteArray());
        reset();
    }

    @Override
    public void reset() {
        fifo.clear();
        crc = CRC16.INITIAL_VALUE;
    }
}
