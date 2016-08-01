package com.invertor.modbus.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
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

    public byte[] getByteBuffer() {
        return baos.getByteBuffer();
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

    private class ByteArrayOutputStream extends java.io.ByteArrayOutputStream {

        ByteArrayOutputStream(int size) {
            super(size);
        }

        byte[] getByteBuffer() {
            return buf;
        }
    }
}
