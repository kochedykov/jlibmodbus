package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.exception.ModbusIOException;
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
public class ModbusMasterConnectionTCP extends ModbusConnection {

    final private TcpParameters parameters;
    private Socket socket = null;
    private ModbusTransport transport = null;
    private int readTimeout = Modbus.MAX_RESPONSE_TIMEOUT;

    public ModbusMasterConnectionTCP(TcpParameters parameters) {
        this.parameters = parameters;
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
        if (parameters != null) {
            close();
            socket = new Socket();
            InetSocketAddress isa = new InetSocketAddress(parameters.getHost(), parameters.getPort());
            try {
                socket.setKeepAlive(parameters.isKeepAlive());
                socket.connect(isa, Modbus.MAX_CONNECTION_TIMEOUT);
            } catch (Exception e) {
                throw new ModbusIOException(e);
            }
        }
        transport = new ModbusTransportTCP(socket);
        setReadTimeout(readTimeout);
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
        readTimeout = timeout;
        if (transport != null)
            transport.getInputStream().setReadTimeout(timeout);
    }
}
