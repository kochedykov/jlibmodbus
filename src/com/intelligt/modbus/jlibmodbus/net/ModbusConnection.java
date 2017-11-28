package com.intelligt.modbus.jlibmodbus.net;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingOutputStream;
import com.intelligt.modbus.jlibmodbus.net.transport.ModbusTransport;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListenerListImpl;

import java.util.concurrent.atomic.AtomicBoolean;

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
abstract public class ModbusConnection extends FrameEventListenerListImpl {

    private int readTimeout = Modbus.MAX_RESPONSE_TIMEOUT;
    final private AtomicBoolean opened = new AtomicBoolean(false);

    abstract public LoggingOutputStream getOutputStream();

    abstract public LoggingInputStream getInputStream();

    abstract public ModbusTransport getTransport();

    final public void open() throws ModbusIOException {
        openImpl();

        setOpened(true);
        getOutputStream().setListenerList(this);
        getInputStream().setListenerList(this);
    }

    final public void close() throws ModbusIOException {
        try {
            closeImpl();
        } finally {
            setOpened(false);
        }
    }

    abstract protected void openImpl() throws ModbusIOException;

    abstract protected void closeImpl() throws ModbusIOException;

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int timeout) {
        readTimeout = timeout;
        if (getTransport() != null) {
            LoggingInputStream is = getInputStream();
            if (is != null) {
                is.setReadTimeout(timeout);
            }
        }
    }

    @Override
    protected void finalize()
            throws Throwable {
        super.finalize();
        close();
    }

    public boolean isOpened() {
        return opened.get();
    }

    private void setOpened(boolean opened) {
        this.opened.set(opened);
    }
}
