package com.sbpinvertor.conn;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.utils.ByteFifo;
import jssc.SerialPortList;

import java.io.IOException;
import java.util.Arrays;

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
    final private String device;
    final private int baudRate;
    final private int dataBits;
    final private int stopBits;
    final private int parity;

    final private ByteFifo fifo = new ByteFifo(Modbus.MAX_RTU_ADU_LENGTH);

    public SerialPort(String device, BaudRate baudRate, int dataBits, int stopBits, Parity parity) {
        this.device = device;
        this.baudRate = baudRate.getValue();
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity.getValue();
        this.port = new jssc.SerialPort(device);
    }

    public static String[] getPortList() {
        return SerialPortList.getPortNames();
    }

    public String getDevice() {
        return device;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }

    public void writeByte(int b) throws SerialPortException {
        try {
            port.writeByte((byte) b);
        } catch (Exception e) {
            throw new SerialPortException(e);
        }
    }

    public void writeShort(int b) throws SerialPortException {
        try {
            port.writeByte((byte) (b & 0xff));
            port.writeByte((byte) ((b >> 8) & 0xff));
        } catch (Exception e) {
            throw new SerialPortException(e);
        }
    }

    public void writeShortBig(int b) throws SerialPortException {
        try {
            port.writeByte((byte) ((b >> 8) & 0xff));
            port.writeByte((byte) (b & 0xff));
        } catch (Exception e) {
            throw new SerialPortException(e);
        }
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

    public void write(int b) throws IOException {
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

    private int write(byte[] bytes, int length) throws SerialPortException {
        try {
            if (port.writeBytes(Arrays.copyOf(bytes, length))) {
                return bytes.length;
            }
        } catch (Exception e) {
            throw new SerialPortException(e);
        }
        return 0;
    }

    public int hasBytes() throws SerialPortException {
        int c;
        try {
            c = port.getInputBufferBytesCount();
        } catch (Exception e) {
            throw new SerialPortException(e);
        }
        return c;
    }

    public void open() throws SerialPortException {
        try {
            port.openPort();
            port.setParams(baudRate, dataBits, stopBits, parity);
            port.setFlowControlMode(jssc.SerialPort.FLOWCONTROL_NONE);
        } catch (Exception ex) {
            throw new SerialPortException(ex);
        }
    }

    public int read() throws IOException {
        try {
            return port.readBytes(1, Modbus.MAX_RESPONSE_TIMEOUT)[0];
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int c = -1;
        try {
            byte[] rb = port.readBytes(b.length);
            System.arraycopy(rb, 0, b, 0, b.length);
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
            return NONE;
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

        static public BaudRate getBaudRate(Integer value) {
            for (BaudRate br : BaudRate.values()) {
                if (br.value == value) {
                    return br;
                }
            }
            return BAUD_RATE_4800;
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
