package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.net.stream.InputStreamTCP;
import com.sbpinvertor.modbus.net.stream.OutputStreamTCP;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.net.stream.base.ModbusOutputStream;
import com.sbpinvertor.modbus.tcp.TcpParameters;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

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
public class ModbusConnectionTCP extends ModbusConnection {

    final private TcpParameters parameters;
    private Socket socket = null;
    private InputStreamTCP is = null;
    private OutputStreamTCP os = null;
    private int readTimeout = Modbus.MAX_RESPONSE_TIMEOUT;

    public ModbusConnectionTCP(TcpParameters parameters) {
        this.parameters = parameters;
    }

    public ModbusConnectionTCP(Socket socket) {
        this.parameters = null;
        this.socket = socket;
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
    public void reset() throws ModbusTransportException {
        open();
    }

    @Override
    public void open() throws ModbusTransportException {
        try {
            if (parameters != null) {
                close();
                socket = new Socket();
                socket.setKeepAlive(parameters.isKeepAlive());
                InetSocketAddress isa = new InetSocketAddress(parameters.getHost(), parameters.getPort());
                socket.connect(isa, Modbus.MAX_CONNECTION_TIMEOUT);
            }
            is = new InputStreamTCP(socket);
            os = new OutputStreamTCP(socket);
            is.setReadTimeout(readTimeout);
        } catch (IOException e) {
            throw new ModbusTransportException(e);
        }
    }

    @Override
    public void close() throws ModbusTransportException {
        try {
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            throw new ModbusTransportException(e);
        } finally {
            is = null;
            os = null;
            socket = null;
        }
    }

    @Override
    public void setReadTimeout(int timeout) {
        readTimeout = timeout;
        if (is != null)
            is.setReadTimeout(timeout);
    }
}
