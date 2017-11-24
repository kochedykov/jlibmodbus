package com.intelligt.modbus.jlibmodbus.utils;

import java.io.ByteArrayInputStream;
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
final public class ByteFifo {

    final private ByteArrayOutputStream baos;
    final private ByteArrayInputStream bais;
    final private int capacity;

    public ByteFifo(int size) {
        baos = new ByteArrayOutputStream(size);
        bais = new ByteArrayInputStream(baos.getByteBuffer());
        capacity = size;
        bais.mark(0);
        reset();
    }

    public void reset() {
        baos.reset();
        bais.reset();
    }

    public byte[] toByteArray() {
        return baos.toByteArray();
    }

    public int size() {
        return baos.size();
    }

    public int read() {
        return available() > 0 ? bais.read() : -1;
    }

    public int read(byte[] b) throws IOException {
        return (available() < b.length) ? 0 : bais.read(b);
    }

    public int read(byte[] b, int offset, int length) {
        return (available() < b.length) ? 0 : bais.read(b, offset, length);
    }

    public void write(int b) {
        if (size() < capacity) {
            baos.write(b);
        }
    }

    public void write(byte[] b) {
        int available = (capacity - size());
        if (available > 0) {
            int count = b.length < available ? b.length : available;
            baos.write(b, 0, count);
        }
    }

    public void write(byte[] b, int off, int len) {
        int available = (capacity - size());
        if (available > 0) {
            int count = len < available ? len : available;
            baos.write(b, off, count);
        }
    }

    public int available() {
        return size() - (capacity - bais.available());
    }

    static private class ByteArrayOutputStream extends java.io.ByteArrayOutputStream {

        ByteArrayOutputStream(int size) {
            super(size);
        }

        byte[] getByteBuffer() {
            return buf;
        }
    }
}
