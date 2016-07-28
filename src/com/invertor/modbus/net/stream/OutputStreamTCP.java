package com.invertor.modbus.net.stream;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;

import java.io.BufferedOutputStream;
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
public class OutputStreamTCP extends ModbusOutputStream {

    final private BufferedOutputStream os;

    public OutputStreamTCP(Socket s) throws ModbusIOException {
        try {
            this.os = new BufferedOutputStream(s.getOutputStream());
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
    }

    @Override
    public void flush() throws IOException {
        os.write(toByteArray());
        os.flush();
        super.flush();
    }

    @Override
    public void close() throws IOException {
        os.close();
    }
}