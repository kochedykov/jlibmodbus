package com.invertor.modbus.net.stream;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.net.stream.base.LoggingInputStream;
import com.invertor.modbus.net.stream.base.ModbusInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Copyright (c) 2015-2016 JSC Invertor
 * [http://www.sbp-invertor.ru]
 * <p/>
 * This file is part of JLibModbus.
 * <p/>
 * JLibModbus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
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
                    throw new IOException();
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
