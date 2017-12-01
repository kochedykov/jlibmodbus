package com.intelligt.modbus.jlibmodbus.net.stream;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

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
public class InputStreamTCP extends LoggingInputStream {

    public InputStreamTCP(final Socket s) throws IOException {
        super(new ModbusInputStream() {

            final private Socket socket = s;
            final private BufferedInputStream in = new BufferedInputStream(s.getInputStream());

            @Override
            public int read() throws IOException {
                int c = in.read();
                if (-1 == c) {
                    throw new IOException("Input stream is closed");
                }
                return c;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                int count = 0;
                int k = 0;
                while (count < len && k != -1) {
                    k = in.read(b, off + count, len - count);
                    if (-1 != k)
                        count += k;
                }
                return count;
            }

            @Override
            public void setReadTimeout(int readTimeout) {
                try {
                    socket.setSoTimeout(readTimeout);
                } catch (SocketException e) {
                    Modbus.log().warning(e.getLocalizedMessage());
                }
            }

            @Override
            public void close() throws IOException {
                in.close();
            }
        });
    }
}
