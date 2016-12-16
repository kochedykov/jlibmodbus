package com.invertor.modbus.serial;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;

import java.io.IOException;

/**
 * Copyright (c) 2015-2016 JSC Invertor
 * [http://www.sbp-invertor.ru]
 * <p/>
 * This file is part of JUniMonitor.
 * <p/>
 * JUniMonitor is free software: you can redistribute it and/or modify
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

public abstract class SerialPort {

    final private SerialParameters serialParameters;
    private int readTimeout = Modbus.MAX_RESPONSE_TIMEOUT;

    public SerialPort(SerialParameters sp) throws SerialPortException {
        this.serialParameters = sp;
    }

    public SerialParameters getSerialParameters() {
        return serialParameters;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public ModbusOutputStream getOutputStream() {
        return new ModbusOutputStream() {
            final private SerialPort serial = SerialPort.this;

            @Override
            public void flush() throws IOException {
                try {
                    serial.write(toByteArray());
                } catch (Exception e) {
                    throw new IOException(e);
                } finally {
                    super.flush();
                }
            }
        };
    }

    public ModbusInputStream getInputStream() {
        return new ModbusInputStream() {

            final private SerialPort serial = SerialPort.this;

            @Override
            public int read() throws IOException {
                return serial.read() & 0xff;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                int size = 0;
                while (size < len) {
                    size += serial.read(b, size, len - size);
                }
                return size;
            }

            @Override
            public void setReadTimeout(int readTimeout) {
                serial.setReadTimeout(readTimeout);
            }
        };
    }

    public void clear() {
        purgeRx();
        purgeTx();
    }

    abstract public void purgeRx();

    abstract public void purgeTx();

    abstract public void write(int b) throws IOException;

    abstract public void write(byte[] bytes) throws IOException;

    abstract public void open() throws SerialPortException;

    abstract public int read() throws IOException;

    abstract public int read(byte[] b, int off, int len) throws IOException;

    abstract public void close();

    abstract public boolean isOpened();

    public enum Parity {
        NONE(0),
        ODD(1),
        EVEN(2),
        MARK(3),
        SPACE(4);

        private final int value;

        Parity(int value) {
            this.value = value;
        }

        static public Parity getParity(Integer value) {
            for (Parity par : Parity.values()) {
                if (par.value == value) {
                    return par;
                }
            }
            throw new IllegalArgumentException("Illegal parity value:" + value);
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum BaudRate {
        BAUD_RATE_4800(4800),
        BAUD_RATE_9600(9600),
        BAUD_RATE_1440(14400),
        BAUD_RATE_19200(19200),
        BAUD_RATE_38400(38400),
        BAUD_RATE_57600(57600),
        BAUD_RATE_115200(115200);

        private final int value;

        BaudRate(int value) {
            this.value = value;
        }

        static public BaudRate getBaudRate(int value) {
            for (BaudRate br : BaudRate.values()) {
                if (br.value == value) {
                    return br;
                }
            }
            throw new IllegalArgumentException("Illegal baud rate value:" + value);
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
