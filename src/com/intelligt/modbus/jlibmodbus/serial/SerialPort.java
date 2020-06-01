package com.intelligt.modbus.jlibmodbus.serial;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;

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

public abstract class SerialPort {

    final private SerialParameters serialParameters;
    private int readTimeout = Modbus.MAX_RESPONSE_TIMEOUT;

    public SerialPort(SerialParameters sp) {
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
                    if (serial.isOpened()) {
                        serial.write(toByteArray());
                    }
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
                    size += serial.read(b, off + size, len - size);
                }
                return size;
            }

            @Override
            public void setReadTimeout(int readTimeout) {
                serial.setReadTimeout(readTimeout);
            }
        };
    }

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
        BAUD_RATE_14400(14400),
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
