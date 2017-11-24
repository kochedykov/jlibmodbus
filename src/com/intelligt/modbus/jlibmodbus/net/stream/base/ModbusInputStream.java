package com.intelligt.modbus.jlibmodbus.net.stream.base;

import com.intelligt.modbus.jlibmodbus.utils.DataUtils;

import java.io.IOException;
import java.io.InputStream;

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
 * extends the <code>InputStream</code> class with <code>ModbusInputStream#readShortLE()</code>
 * and <code>ModbusInputStream#readShortBE()</code> methods. Also it allows to set max read
 * operation time with <code>ModbusInputStream#setReadTimeout()</code> method.
 *
 * @author kochedykov
 * @since 1.0
 */
abstract public class ModbusInputStream extends InputStream {

    /**
     * default implementation of reading of a byte array.
     *
     * @param b a byte array to fill by values from the input stream.
     * @return total number of values read from the input stream into the byte array
     * @throws IOException          if there is a read operation timeout
     * @throws NullPointerException if <code>b</code> is <code>null</code>.
     */
    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * sets a timeout value. if there is a timeout of read operation a stream must throw new IO exception.
     *
     * @param readTimeout is a timeout value in milliseconds
     */
    abstract public void setReadTimeout(int readTimeout);

    /**
     * read two bytes in Big Endian Byte Order
     *
     * @return 16-bit value placed in first two bytes of integer value, second two bytes is equal zero.
     * @throws IOException
     * @see ModbusInputStream#readShortLE()
     */
    public int readShortBE() throws IOException {
        int h = read();
        int l = read();
        if (-1 == h || -1 == l)
            return -1;
        return DataUtils.toShort(h, l);
    }

    /**
     * read two bytes in Little Endian Byte Order
     *
     * @return 16-bit value placed in first two bytes of integer value, second two bytes is equal zero.
     * @throws IOException
     * @see ModbusInputStream#readShortBE()
     */
    public int readShortLE() throws IOException {
        int l = read();
        int h = read();
        if (-1 == h || -1 == l)
            return -1;
        return DataUtils.toShort(h, l);
    }
}
