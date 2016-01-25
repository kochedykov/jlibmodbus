package com.sbpinvertor.modbus.net.streaming;

import com.sbpinvertor.conn.SerialPort;
import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.data.ModbusInputStream;
import com.sbpinvertor.modbus.net.ModbusTransport;
import com.sbpinvertor.modbus.utils.ByteFifo;
import com.sbpinvertor.modbus.utils.DataUtils;

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
public class InputStreamASCII extends ModbusInputStream {
    final private SerialPort serial;
    final private ModbusTransport transport;
    final private ByteFifo fifo = new ByteFifo(Modbus.MAX_RTU_ADU_LENGTH);

    public InputStreamASCII(SerialPort serial, ModbusTransport transport) {
        this.serial = serial;
        this.transport = transport;
    }

    @Override
    public int read() throws IOException {
        int b;
        char c = (char) readByte();
        if (c == Modbus.ASCII_CODE_COLON) {
            fifo.clear();
            c = (char) readByte();
        }
        b = DataUtils.fromAscii(c, (char) readByte());
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

    public int readByte() throws IOException {
        return serial.readByte(transport.getResponseTimeout());
    }
}