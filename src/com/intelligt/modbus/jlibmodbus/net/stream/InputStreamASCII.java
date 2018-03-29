package com.intelligt.modbus.jlibmodbus.net.stream;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusChecksumException;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;

import java.io.IOException;

/*
 * Copyright (C) 2016 "Invertor" Factory", JSC
 * [http://www.sbp-invertor.ru]
 *
 * This file is part of JLibModbus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
        int c_lrc = (byte) lrcGet();
        int r_lrc = (byte) read();

        int cr = readRaw();
        int lf = readRaw();
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