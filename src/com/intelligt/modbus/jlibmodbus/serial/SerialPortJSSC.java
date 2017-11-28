package com.intelligt.modbus.jlibmodbus.serial;

import java.io.IOException;

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

public class SerialPortJSSC extends SerialPort {

    final private jssc.SerialPort port;

    public SerialPortJSSC(SerialParameters sp) {
        super(sp);
        this.port = new jssc.SerialPort(sp.getDevice());
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
        try {
            byte[] rb = port.readBytes(len, getReadTimeout());
            System.arraycopy(rb, 0, b, off, len);
            return rb.length;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() {
        if (isOpened()) {
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
