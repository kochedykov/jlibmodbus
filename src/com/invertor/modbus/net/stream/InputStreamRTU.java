package com.invertor.modbus.net.stream;

import com.invertor.modbus.serial.SerialPort;
import com.invertor.modbus.utils.CRC16;

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
public class InputStreamRTU extends InputStreamSerial {

    private int crc;

    public InputStreamRTU(SerialPort serial) {
        super(serial);
        crc = CRC16.INITIAL_VALUE;
    }

    @Override
    public void reset() {
        crc = CRC16.INITIAL_VALUE;
    }

    @Override
    public int read() throws IOException {
        int b = super.read();
        crc = CRC16.calc(crc, (byte) b);
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int c = super.read(b, off, len);
        crc = CRC16.calc(crc, b, off, len);
        return c;
    }

    public int getCrc() {
        return crc;
    }
}