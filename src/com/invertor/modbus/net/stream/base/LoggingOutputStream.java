package com.invertor.modbus.net.stream.base;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.utils.ByteFifo;
import com.invertor.modbus.utils.DataUtils;

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

    final static private String LOG_MESSAGE_TITLE = "Frame sent: ";
    /**
     * The output stream to be logged
     */
    final private ModbusOutputStream out;
    final private ByteFifo fifo = new ByteFifo(Modbus.MAX_PDU_LENGTH);

    public LoggingOutputStream(ModbusOutputStream out) {
        this.out = out;
    }


    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
        if (Modbus.isLoggingEnabled()) {
            fifo.write(b);
        }
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        if (Modbus.isLoggingEnabled()) {
            fifo.write(b);
        }
    }

    @Override
    public void flush() throws IOException {
        out.flush();
        log();
    }

    public void log() {
        if (Modbus.isLoggingEnabled()) {
            Modbus.log().info(new StringBuilder().append(LOG_MESSAGE_TITLE).append(DataUtils.toAscii(fifo.toByteArray())).toString());
            fifo.reset();
        }
    }

    public byte[] toByteArray() {
        return out.toByteArray();
    }
}
