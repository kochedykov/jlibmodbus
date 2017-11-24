package com.intelligt.modbus.jlibmodbus.net.stream;

import com.intelligt.modbus.jlibmodbus.exception.ModbusChecksumException;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.utils.CRC16;

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
public class InputStreamRTU extends InputStreamSerial {

    private int crc = CRC16.INITIAL_VALUE;

    public InputStreamRTU(SerialPort serial) {
        super(serial);
    }

    @Override
    public void frameCheck() throws IOException, ModbusChecksumException {
        int c_crc = getCrc();
        int r_crc = readShortLE();

        if (c_crc != r_crc) {
            throw new ModbusChecksumException(r_crc, c_crc);
        }
    }

    @Override
    public void frameInit() {
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

    private int getCrc() {
        return crc;
    }
}