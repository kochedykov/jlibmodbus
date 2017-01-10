package com.invertor.modbus.serial;

import com.invertor.modbus.Modbus;

import javax.comm.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

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

public class SerialPortJavaComm extends com.invertor.modbus.serial.SerialPort implements SerialPortEventListener {

    private javax.comm.SerialPort port;
    private AtomicBoolean opened = new AtomicBoolean(false);
    private InputStream in;
    private OutputStream out;


    public SerialPortJavaComm(SerialParameters sp) throws SerialPortException {
        super(sp);
    }

    @Override
    public void purgeRx() {
        if (isOpened()) {
            try {
                in.skip(in.available());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void purgeTx() {
        if (isOpened()) {
            try {
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void write(int b) throws IOException {
        if (!isOpened()) {
            throw new IOException("Port not opened");
        }
        try {
            out.write((byte) b);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        if (!isOpened()) {
            throw new IOException("Port not opened");
        }
        try {
            out.write(bytes);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void open() throws SerialPortException {
        try {
            SerialParameters sp = getSerialParameters();

            CommPortIdentifier portIdentifier;

            portIdentifier = CommPortIdentifier.getPortIdentifier(sp.getDevice());
            if (portIdentifier.isCurrentlyOwned()) {
                String msg = "Cannot open serial port " + sp.getDevice();
                Modbus.log().warning(msg);
                throw new SerialPortException(msg);
            } else {
                CommPort commPort = portIdentifier.open(this.getClass().getName(), Modbus.MAX_CONNECTION_TIMEOUT);

                if (commPort instanceof javax.comm.SerialPort) {
                    port = (javax.comm.SerialPort) commPort;
                    port.setSerialPortParams(sp.getBaudRate(), sp.getDataBits(), sp.getStopBits(), sp.getParity().getValue());
                    port.setFlowControlMode(javax.comm.SerialPort.FLOWCONTROL_NONE);

                    in = port.getInputStream();
                    out = port.getOutputStream();

                    port.enableReceiveTimeout(Modbus.MAX_RESPONSE_TIMEOUT);
                    setOpened(true);
                } else {
                    Modbus.log().severe(sp.getDevice() + " is not a serial port.");
                }
            }
        } catch (NoSuchPortException e) {
            throw new SerialPortException(e);
        } catch (Exception ex) {
            throw new SerialPortException(ex);
        }
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        super.setReadTimeout(readTimeout);
        try {
            port.enableReceiveTimeout(readTimeout);
        } catch (UnsupportedCommOperationException e) {
            Modbus.log().warning(e.getLocalizedMessage());
        }
    }

    @Override
    public int read() throws IOException {
        if (!isOpened()) {
            throw new IOException("Port not opened");
        }
        int c;
        try {
            c = in.read();
        } catch (Exception e) {
            throw new IOException(e);
        }
        if (c > -1)
            return c;
        else
            throw new IOException("Read timeout");
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (!isOpened()) {
            throw new IOException("Port not opened");
        }
        int c;
        try {
            c = in.read(b, off, len);
        } catch (Exception e) {
            throw new IOException(e);
        }
        if (c > -1)
            return c;
        else
            throw new IOException("Read timeout");
    }

    @Override
    public void close() {
        try {
            if (isOpened()) {
                setOpened(false);

                in.close();
                out.close();
                port.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isOpened() {
        return opened.get();
    }

    public void setOpened(boolean opened) {
        this.opened.set(opened);
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            case SerialPortEvent.OE:
            case SerialPortEvent.PE:
            case SerialPortEvent.FE:
                Modbus.log().warning(port.getName() + ": framing error.");
        }

    }
}
