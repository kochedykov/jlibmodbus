package com.invertor.modbus.net.stream;

import com.invertor.modbus.Modbus;
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
public class OutputStreamASCII extends OutputStreamSerial {

    private int lrc = 0;

    public OutputStreamASCII(SerialPort serial) {
        super(serial);
        reset();
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        for (byte b : bytes) {
            lrc += b;
        }
        byte[] ascii = DataUtils.toAscii(bytes).getBytes();
        super.write(ascii);
    }

    @Override
    public void write(int b) throws IOException {
        lrc += (byte) b;
        byte[] bytes = DataUtils.toAscii((byte) b).getBytes();
        super.write(bytes);
    }

    private void writeChecksum() throws IOException {
        byte[] bytes = DataUtils.toAscii((byte) -lrc).getBytes();
        super.write(bytes);
    }

    public void writeRaw(int b) throws IOException {
        super.write(b);
    }

    @Override
    public void flush() throws IOException {
        writeChecksum();
        writeRaw(Modbus.ASCII_CODE_CR);
        writeRaw(Modbus.ASCII_CODE_LF);
        super.flush();
        reset();
    }

    public void reset() {
        try {
            lrc = 0;
            writeRaw(Modbus.ASCII_CODE_COLON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
