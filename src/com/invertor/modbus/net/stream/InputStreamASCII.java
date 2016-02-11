package com.invertor.modbus.net.stream;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.serial.SerialPort;
import com.invertor.modbus.utils.ByteFifo;
import com.invertor.modbus.utils.DataUtils;

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
public class InputStreamASCII extends InputStreamSerial {
    final private ByteFifo fifo = new ByteFifo(Modbus.MAX_RTU_ADU_LENGTH);

    public InputStreamASCII(SerialPort serial) {
        super(serial);
    }

    public int readRaw() throws IOException {
        return super.read();
    }

    @Override
    public int read() throws IOException {
        int b;
        char c = (char) readRaw();
        if (c == Modbus.ASCII_CODE_COLON) {
            fifo.clear();
            c = (char) readRaw();
        }
        b = DataUtils.fromAscii(c, (char) readRaw());
        fifo.write(b);
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        for (int i = off; i < len; i++) {
            b[i] = (byte) read();
        }
        return len;
    }

    public int getLrc() {
        int lrc = 0;
        byte[] buffer = fifo.getByteBuffer();
        for (int i = 0; i < fifo.size(); i++) {
            lrc += buffer[i];
        }
        return (byte) (-lrc) & 0xff;
    }

    @Override
    public void reset() {
        super.reset();
        fifo.clear();
    }
}