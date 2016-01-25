package com.sbpinvertor.modbus.net.streaming;

import com.sbpinvertor.modbus.data.ModbusInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright (c) 2015-2016 JSC "Zavod "Invertor"
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

    volatile private BufferedInputStream is;

    public InputStreamTCP(InputStream is) {
        this.is = new BufferedInputStream(is);
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
}
