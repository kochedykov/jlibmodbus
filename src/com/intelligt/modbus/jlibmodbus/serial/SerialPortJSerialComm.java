package com.intelligt.modbus.jlibmodbus.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

public class SerialPortJSerialComm extends SerialPort {

    private com.fazecast.jSerialComm.SerialPort port;
    private InputStream in;
    private OutputStream out;
    final byte [] b = new byte[1];

    public SerialPortJSerialComm(SerialParameters sp) {
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
        SerialParameters sp = getSerialParameters();
        port = com.fazecast.jSerialComm.SerialPort.getCommPort(sp.getDevice());
        port.openPort();

        port.setComPortParameters(sp.getBaudRate(), sp.getDataBits(), sp.getStopBits(), sp.getParity().getValue());
        port.setFlowControl(com.fazecast.jSerialComm.SerialPort.FLOW_CONTROL_DISABLED);

        in = port.getInputStream();
        out = port.getOutputStream();

        port.setComPortTimeouts(com.fazecast.jSerialComm.SerialPort.TIMEOUT_READ_BLOCKING, getReadTimeout(), 0);

    }

    @Override
    public void setReadTimeout(int readTimeout) {
        super.setReadTimeout(readTimeout);

        if (isOpened()) {
            port.setComPortTimeouts(com.fazecast.jSerialComm.SerialPort.TIMEOUT_NONBLOCKING, getReadTimeout(), getReadTimeout());
        }
    }

    @Override
    public int read() throws IOException {
        if (!isOpened()) {
            throw new IOException("Port not opened");
        }
        int c;

        try {
            c = in.read(b, 0, b.length);
        } catch (Exception e) {
            throw new IOException(e);
        }
        if (c > 0)
            return b[0];
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
                in.close();
                out.close();
                port.closePort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isOpened() {
        return port.isOpen();
    }
}
