package com.sbpinvertor.modbus;

import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.msg.base.ModbusMessage;
import com.sbpinvertor.modbus.net.ModbusConnection;
import com.sbpinvertor.modbus.net.ModbusConnectionTCP;
import com.sbpinvertor.modbus.net.ModbusTransport;
import com.sbpinvertor.modbus.net.ModbusTransportTCP;
import com.sbpinvertor.modbus.tcp.TcpParameters;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

public class ModbusMasterTCP extends ModbusMaster {
    final private boolean keepAlive;
    final private ModbusConnection conn;
    final private AtomicBoolean connected = new AtomicBoolean(false);
    private ModbusTransport transport = null;

    public ModbusMasterTCP(TcpParameters parameters) throws ModbusTransportException {
        conn = new ModbusConnectionTCP(parameters);
        keepAlive = parameters.isKeepAlive();
        if (keepAlive) {
            open();
        }
    }

    public ModbusMasterTCP(String host, int port, boolean keepAlive) throws ModbusTransportException {
        this(new TcpParameters(host, port, keepAlive));
    }

    public ModbusMasterTCP(String host, boolean keepAlive) throws ModbusTransportException {
        this(host, Modbus.TCP_PORT, keepAlive);
    }

    public ModbusMasterTCP(String host) throws ModbusTransportException {
        this(host, false);
    }

    @Override
    protected void sendRequest(ModbusMessage msg) throws ModbusTransportException, IOException {
        if (!keepAlive)
            open();
        try {
            getTransport().send(msg);
        } catch (IOException e) {
            if (keepAlive) {
                open();
                transport.send(msg);
            }
        }
    }

    @Override
    protected ModbusMessage readResponse() throws ModbusTransportException, ModbusNumberException, IOException {
        ModbusMessage msg = getTransport().readResponse();
        if (!keepAlive) {
            close();
        }

        return msg;
    }

    @Override
    public void open() throws ModbusTransportException {
        if (!isConnected()) {
            conn.open();
            transport = new ModbusTransportTCP(conn.getInputStream(), conn.getOutputStream());
            setConnected(true);
        }
    }

    @Override
    public void close() throws ModbusTransportException {
        setConnected(false);
        conn.close();
        transport = null;
    }

    synchronized public boolean isConnected() {
        return connected.get();
    }

    public void setConnected(boolean connected) {
        this.connected.set(connected);
    }

    @Override
    protected ModbusTransport getTransport() {
        return transport;
    }

    @Override
    protected ModbusConnection getConnection() {
        return conn;
    }
}
