package com.invertor.modbus.net.stream;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.net.stream.base.ModbusInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

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
public class InputStreamTCP extends ModbusInputStream {

    final private BufferedInputStream is;

    public InputStreamTCP(Socket s) throws ModbusIOException {
        try {
            this.is = new BufferedInputStream(s.getInputStream());
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
    }

    @Override
    public int read() throws IOException {
        int c = is.read();
        if (c == -1) {
            c = is.read();
        }
        return c;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = 0;
        int k = 0;
        while (count < len && k != -1) {
            k = is.read(b, off + count, len - count);
            if (-1 != k)
                count += k;
        }
        return count;
    }

    @Override
    public void reset() {
        //dummy
    }

    @Override
    public void setReadTimeout(int readTimeout) {
        //no op
    }
}
