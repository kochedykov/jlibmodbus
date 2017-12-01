package com.intelligt.modbus.jlibmodbus.net.stream.base;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListenerList;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListenerListImpl;

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

/**
 * this class allows to log a content of the output stream before it'll be flushed.
 *
 * @author kochedykov
 * @since 1.2
 */
public class LoggingOutputStream extends ModbusOutputStream {

    private FrameEventListenerList listenerList = new FrameEventListenerListImpl();
    final static private String LOG_MESSAGE_TITLE = "Frame sent: ";
    /**
     * The output stream to be logged
     */
    final private ModbusOutputStream out;

    public LoggingOutputStream(ModbusOutputStream out) {
        this.out = out;
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
        if (Modbus.isLoggingEnabled()) {
            super.write(b);
        }
    }

    @Override
    public void write(byte[] b, int offset, int length) throws IOException {
        out.write(b, offset, length);
        if (Modbus.isLoggingEnabled()) {
            super.write(b, offset, length);
        }
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        if (Modbus.isLoggingEnabled()) {
            super.write(b);
        }
    }

    @Override
    public void flush() throws IOException {
        out.flush();
        log();
    }

    public void log() {
        if (Modbus.isLoggingEnabled() && super.getFifo().size() > 0) {
            byte[] bytes = super.toByteArray();
            Modbus.log().info(LOG_MESSAGE_TITLE + DataUtils.toAscii(bytes));
            listenerList.fireFrameSentEvent(new FrameEvent(bytes));
            super.getFifo().reset();
        }
    }

    public byte[] toByteArray() {
        return out.toByteArray();
    }

    public void setListenerList(FrameEventListenerList listenerList) {
        this.listenerList = listenerList;
    }
}
