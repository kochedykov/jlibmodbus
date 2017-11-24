package com.intelligt.modbus.jlibmodbus.serial;

import com.intelligt.modbus.jlibmodbus.Modbus;
import purejavacomm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Copyright (C) 2017 Vladislav Y. Kochedykov
 *
 * [http://jlibmodbus.sourceforge.net]
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

public class SerialPortPJC extends SerialPort implements SerialPortEventListener {

    private purejavacomm.SerialPort port;
    final private AtomicBoolean opened = new AtomicBoolean(false);
    private InputStream in;
    private OutputStream out;


    public SerialPortPJC(SerialParameters sp) {
        super(sp);
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

                if (commPort instanceof purejavacomm.SerialPort) {
                    port = (purejavacomm.SerialPort) commPort;
                    port.setSerialPortParams(sp.getBaudRate(), sp.getDataBits(), sp.getStopBits(), sp.getParity().getValue());
                    port.setFlowControlMode(gnu.io.SerialPort.FLOWCONTROL_NONE);

                    in = port.getInputStream();
                    out = port.getOutputStream();

                    port.enableReceiveTimeout(getReadTimeout());
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
            if (isOpened())
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
                break;
            default:
                break;
        }

    }
}
