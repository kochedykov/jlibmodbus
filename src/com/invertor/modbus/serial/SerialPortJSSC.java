package com.invertor.modbus.serial;

import java.io.IOException;

/**
 * Copyright (c) 2015-2017 JLibModbus developers team
 * <p/>
 * [http://jlibmodbus.sourceforge.net]
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

public class SerialPortJSSC extends SerialPort{

    final private jssc.SerialPort port;

    public SerialPortJSSC(SerialParameters sp) {
        super(sp);
        this.port = new jssc.SerialPort(sp.getDevice());
    }

    @Override
    public void purgeRx() {
        try {
            port.purgePort(jssc.SerialPort.PURGE_RXCLEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void purgeTx() {
        try {
            port.purgePort(jssc.SerialPort.PURGE_TXCLEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(int b) throws IOException {
        try {
            port.writeByte((byte) b);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        try {
            port.writeBytes(bytes);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void open() throws SerialPortException {
        try {
            port.openPort();
            SerialParameters sp = getSerialParameters();
            port.setParams(sp.getBaudRate(), sp.getDataBits(), sp.getStopBits(), sp.getParity().getValue());
            port.setFlowControlMode(jssc.SerialPort.FLOWCONTROL_NONE);
        } catch (Exception ex) {
            throw new SerialPortException(ex);
        }
    }

    @Override
    public int read() throws IOException {
        try {
            if (getReadTimeout() > 0)
                return port.readBytes(1, getReadTimeout())[0];
            return port.readBytes(1)[0];
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int c = -1;
        try {
            byte[] rb = port.readBytes(b.length);
            System.arraycopy(rb, 0, b, off, len);
            c = rb.length;
        } catch (Exception e) {
            new IOException(e);
        }
        return c;
    }

    @Override
    public void close() {
        if (port.isOpened()) {
            try {
                port.closePort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isOpened() {
        return port.isOpened();
    }
}
