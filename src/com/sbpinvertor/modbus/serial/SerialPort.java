package com.sbpinvertor.modbus.serial;

import java.io.IOException;

/**
 * Copyright (c) 2015-2016 JSC "Zavod "Invertor"
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

public class SerialPort {

    final private jssc.SerialPort port;
    final private SerialParameters sp;

    SerialPort(SerialParameters sp) {
        this.sp = sp;
        this.port = new jssc.SerialPort(sp.getDevice());
    }

    public void clear() {
        purgeRx();
        purgeTx();
    }

    public void purgeRx() {
        try {
            port.purgePort(jssc.SerialPort.PURGE_RXCLEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void purgeTx() {
        try {
            port.purgePort(jssc.SerialPort.PURGE_TXCLEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(int b) {
        try {
            port.writeByte((byte) b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(byte[] bytes) {
        try {
            port.writeBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() throws SerialPortException {
        try {
            port.openPort();
            port.setParams(sp.getBaudRate(), sp.getDataBits(), sp.getStopBits(), sp.getParity().getValue());
            port.setFlowControlMode(jssc.SerialPort.FLOWCONTROL_NONE);
        } catch (Exception ex) {
            throw new SerialPortException(ex);
        }
    }

    public int readByte(int timeout) throws IOException {
        try {
            if (timeout > 0)
                return port.readBytes(1, timeout)[0];
            return port.readBytes(1)[0];
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public int read(byte[] b, int off, int len) {
        int c = -1;
        try {
            byte[] rb = port.readBytes(b.length);
            System.arraycopy(rb, 0, b, off, len);
            c = rb.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public void close() {
        if (port.isOpened()) {
            try {
                port.closePort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
            throw new NumberFormatException("Illegal parity value:" + value);
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
            throw new NumberFormatException("Illegal baud rate value:" + value);
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
