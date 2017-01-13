package com.invertor.modbus.net;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.net.transport.ModbusTransport;

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
abstract public class ModbusConnection {

    private int readTimeout = Modbus.MAX_RESPONSE_TIMEOUT;
    private AtomicBoolean opened = new AtomicBoolean(false);

    abstract public ModbusOutputStream getOutputStream();

    abstract public ModbusInputStream getInputStream();

    abstract public ModbusTransport getTransport();

    abstract public void open() throws ModbusIOException;

    abstract public void close() throws ModbusIOException;

    //abstract public void reset() throws ModbusIOException;

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int timeout) {
        readTimeout = timeout;
        if (getTransport() != null) {
            ModbusInputStream is = getInputStream();
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

    public void setOpened(boolean opened) {
        this.opened.set(opened);
    }
}
