package com.invertor.modbus.net.stream;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusChecksumException;
import com.invertor.modbus.serial.SerialPort;
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

    private int lrc = 0;

    public InputStreamASCII(SerialPort serial) {
        super(serial);
    }

    private void lrcAdd(byte b) {
        lrc += b;
    }

    private int lrcGet() {
        return (byte) -lrc;
    }

    @Override
    public void frameCheck() throws IOException, ModbusChecksumException {
        int cr;
        int lf;
        int r_lrc;
        int c_lrc = (byte) lrcGet();
        r_lrc = (byte) read();
        cr = readRaw();
        lf = readRaw();
        // following checks delimiter has read (LF character by default)
        if (cr != Modbus.ASCII_CODE_CR || lf != Modbus.getAsciiMsgDelimiter())
            Modbus.log().warning("\\r\\n not received.");
        if (c_lrc != r_lrc) {
            throw new ModbusChecksumException(r_lrc, c_lrc);
        }
    }

    @Override
    public void frameInit() throws IOException {
        lrc = 0;
        char c = (char) readRaw();
        if (c != Modbus.ASCII_CODE_COLON) {
            getSerialPort().purgeRx();
            throw new IOException("no bytes read");
        }
    }

    public int readRaw() throws IOException {
        return super.read();
    }

    @Override
    public int read() throws IOException {
        int b;
        char c = (char) readRaw();
        b = DataUtils.fromAscii(c, (char) readRaw());
        lrcAdd((byte) b);
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        for (int i = off; i < len; i++) {
            b[i] = (byte) read();
        }
        return len;
    }
}