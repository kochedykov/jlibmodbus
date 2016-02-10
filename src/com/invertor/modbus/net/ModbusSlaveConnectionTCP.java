package com.invertor.modbus.net;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;

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
public class ModbusSlaveConnectionTCP extends ModbusConnection {

    private Socket socket;
    private ModbusTransport transport = null;

    public ModbusSlaveConnectionTCP(Socket socket) throws ModbusIOException {
        this.socket = socket;
        transport = new ModbusTransportTCP(socket);
    }

    @Override
    public ModbusOutputStream getOutputStream() {
        return transport.getOutputStream();
    }

    @Override
    public ModbusInputStream getInputStream() {
        return transport.getInputStream();
    }

    @Override
    public ModbusTransport getTransport() {
        return transport;
    }

    @Override
    public void reset() throws ModbusIOException {
        open();
    }

    @Override
    public void open() throws ModbusIOException {
        //no operation
    }

    @Override
    public void close() throws ModbusIOException {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            throw new ModbusIOException(e);
        } finally {
            transport = null;
            socket = null;
        }
    }

    @Override
    public void setReadTimeout(int timeout) {
        if (transport != null)
            transport.getInputStream().setReadTimeout(timeout);
        if (socket != null) {
            try {
                socket.setSoTimeout(timeout);
            } catch (SocketException e) {
                Modbus.log().warning("can't set so timeout.");
            }
        }
    }
}
