package com.sbpinvertor.modbus.net;

import com.sbpinvertor.conn.SerialPort;
import com.sbpinvertor.conn.SerialPortException;
import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.data.ModbusInputStream;
import com.sbpinvertor.modbus.data.ModbusOutputStream;
import com.sbpinvertor.modbus.data.base.ModbusMessage;
import com.sbpinvertor.modbus.data.base.ModbusResponse;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ModbusTransportASCII extends ModbusTransport {

    final static public int ASCII_CODE_CR = 0xd;
    final static public int ASCII_CODE_LF = 0xa;
    final static public int ASCII_CODE_COLON = 0x3a;

    final private AsciiOutputStream os;
    final private AsciiInputStream is;

    public ModbusTransportASCII(SerialPort port) throws SerialPortException {
        os = new AsciiOutputStream(port);
        is = new AsciiInputStream(port);
        port.open();
    }

    @Override
    public ModbusResponse sendRequest(ModbusMessage msg) throws ModbusTransportException {
        ModbusResponse response;
        try {
            os.clear();
            is.clear();
            response = super.sendRequest(msg);
            if (is.getLrc() != is.read())
                throw new ModbusTransportException("LRC check failed.");
            if (is.readByte() != ASCII_CODE_CR || is.readByte() != ASCII_CODE_LF)
                throw new ModbusTransportException("\\r\\n not received.");
        } catch (Exception e) {
            throw new ModbusTransportException(e);
        }
        return response;
    }

    @Override
    public ModbusOutputStream getOutputStream() {
        return os;
    }

    @Override
    public ModbusInputStream getInputStream() {
        return is;
    }

    private class AsciiInputStream extends ModbusInputStream {
        final private SerialPort serial;
        final private ByteFifo fifo = new ByteFifo(Modbus.MAX_RTU_ADU_LENGTH);

        protected AsciiInputStream(SerialPort serial) {
            this.serial = serial;
        }

        public void clear() throws IOException {
            serial.clear();
        }

        @Override
        public int read() throws IOException {
            int b;
            char c = (char) readByte();
            if (c == ASCII_CODE_COLON) {
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

        private int readByte() throws IOException {
            return serial.readByte(getResponseTimeout());
        }
    }

    private class AsciiOutputStream extends ModbusOutputStream {

        final private SerialPort serial;
        final private ByteFifo fifo = new ByteFifo(Modbus.MAX_RTU_ADU_LENGTH);
        private int lrc = 0;

        protected AsciiOutputStream(SerialPort serial) {
            this.serial = serial;
        }

        @Override
        public void write(byte[] bytes) throws IOException {
            fifoInit();
            for (byte b : bytes) {
                lrc += b;
            }
            byte[] ascii = DataUtils.toAscii(bytes);
            fifo.write(ascii);
        }

        @Override
        public void write(int b) throws IOException {
            fifoInit();
            lrc += (byte) b;
            byte[] bytes = DataUtils.toAscii((byte) b);
            fifo.write(bytes);
        }

        @Override
        public void flush() throws IOException {
            fifo.write(DataUtils.toAscii((byte) (lrc = -lrc)));
            fifo.write(ASCII_CODE_CR);
            fifo.write(ASCII_CODE_LF);
            serial.write(fifo.toByteArray());
            clear();
        }

        public void clear() throws IOException {
            fifo.clear();
        }

        private void fifoInit() throws IOException {
            if (fifo.size() == 0) {
                fifo.write(ASCII_CODE_COLON);
                lrc = 0;
            }
        }
    }
}
