package com.intelligt.modbus.jlibmodbus.net.stream.base;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.utils.*;

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
 * this class allows to log a content of the input stream stream.
 *
 * @author kochedykov
 * @since 1.2
 */
public class LoggingInputStream extends ModbusInputStream {

    private FrameEventListenerList listenerList = new FrameEventListenerListImpl();
    final static private String LOG_MESSAGE_TITLE = "Frame recv: ";
    /**
     * The input stream to be logged
     */
    final private ModbusInputStream in;
    final private ByteFifo fifo = new ByteFifo(Modbus.MAX_PDU_LENGTH);

    public LoggingInputStream(ModbusInputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        int b = in.read();
        if (Modbus.isLoggingEnabled()) {
            fifo.write(b);
        }
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = in.read(b, off, len);
        if (Modbus.isLoggingEnabled()) {
            fifo.write(b, off, read);
        }
        return read;
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        in.setReadTimeout(readTimeout);
    }

    public void log() {
        if (Modbus.isLoggingEnabled() && fifo.size() > 0) {
            byte[] bytes = fifo.toByteArray();
            Modbus.log().info(LOG_MESSAGE_TITLE + DataUtils.toAscii(bytes));
            listenerList.fireFrameReceivedEvent(new FrameEvent(bytes));
            fifo.reset();
        }
    }

    public void setListenerList(FrameEventListenerList listenerList) {
        this.listenerList = listenerList;
    }
}
