package com.invertor.modbus.net.stream.base;

import com.invertor.modbus.utils.DataUtils;

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
abstract public class ModbusInputStream extends InputStream {

    @Override
    abstract public int read() throws IOException;

    @Override
    abstract public int read(byte[] b, int off, int len) throws IOException;

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    abstract public void setReadTimeout(int readTimeout);

    public int readShortBE() throws IOException {
        int h = read();
        int l = read();
        if (-1 == h || -1 == l)
            return -1;
        return DataUtils.toShort(h, l);
    }

    public int readShortLE() throws IOException {
        int l = read();
        int h = read();
        if (-1 == h || -1 == l)
            return -1;
        return DataUtils.toShort(h, l);
    }
}
