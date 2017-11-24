package com.intelligt.modbus.jlibmodbus.net.stream;

import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingOutputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

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
public class OutputStreamTCP extends LoggingOutputStream {

    public OutputStreamTCP(final Socket s) throws IOException {
        super(new ModbusOutputStream() {
            final Socket socket = s;
            final private BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());

            @Override
            public void flush() throws IOException {
                try {
                    byte[] bytes = toByteArray();
                    os.write(bytes);
                    os.flush();
                } catch (Exception e) {
                    throw new IOException(e);
                } finally {
                    super.flush();
                }
            }

            @Override
            public void close() throws IOException {
                os.close();
            }
        });
    }
}