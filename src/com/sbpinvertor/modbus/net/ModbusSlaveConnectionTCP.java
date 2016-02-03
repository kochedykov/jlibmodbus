package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.net.stream.InputStreamTCP;
import com.sbpinvertor.modbus.net.stream.OutputStreamTCP;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.net.stream.base.ModbusOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

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
public class ModbusSlaveConnectionTCP extends ModbusConnection {

    private Socket socket;
    private InputStreamTCP is = null;
    private OutputStreamTCP os = null;

    private int readTimeout = Modbus.MAX_RESPONSE_TIMEOUT;

    public ModbusSlaveConnectionTCP(Socket socket) throws IOException {
        this.socket = socket;
        is = new InputStreamTCP(socket);
        os = new OutputStreamTCP(socket);
    }

    @Override
    public ModbusOutputStream getOutputStream() {
        return os;
    }

    @Override
    public ModbusInputStream getInputStream() {
        return is;
    }

    @Override
    public void reset() throws IOException {
        open();
    }

    @Override
    public void open() throws IOException {
        //no operation
    }

    @Override
    public void close() throws IOException {
        try {
            if (socket != null)
                socket.close();
        } finally {
            is = null;
            os = null;
            socket = null;
        }
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    @Override
    public void setReadTimeout(int timeout) {
        readTimeout = timeout;
        if (is != null)
            is.setReadTimeout(timeout);
        if (socket != null) {
            try {
                socket.setSoTimeout(timeout);
            } catch (SocketException e) {
                Modbus.log().warning("can't set so timeout.");
            }
        }
    }
}
