package com.sbpinvertor.modbus;

import com.sbpinvertor.modbus.exception.ModbusMasterException;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusProtocolException;
import com.sbpinvertor.modbus.msg.base.ModbusMessage;
import com.sbpinvertor.modbus.net.ModbusConnection;
import com.sbpinvertor.modbus.net.ModbusConnectionTCP;
import com.sbpinvertor.modbus.net.ModbusTransport;
import com.sbpinvertor.modbus.net.ModbusTransportTCP;
import com.sbpinvertor.modbus.tcp.TcpParameters;
import com.sbpinvertor.modbus.utils.ModbusExceptionCode;

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

    public ModbusMasterTCP(TcpParameters parameters) {
        conn = new ModbusConnectionTCP(parameters);
        keepAlive = parameters.isKeepAlive();
        try {
            if (keepAlive) {
                open();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ModbusMasterTCP(String host, int port, boolean keepAlive) {
        this(new TcpParameters(host, port, keepAlive));
    }

    @Override
    protected void sendRequest(ModbusMessage msg) throws IOException {
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
    protected ModbusMessage readResponse() throws ModbusNumberException, IOException, ModbusProtocolException {
        ModbusMessage msg = getTransport().readResponse();
        if (!keepAlive) {
            close();
        }
        return msg;
    }

    @Override
    public void open() throws IOException {
        if (!isConnected()) {
            conn.open();
            transport = new ModbusTransportTCP(conn.getInputStream(), conn.getOutputStream());
            setConnected(true);
        }
    }

    @Override
    public void close() throws IOException {
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

    @Override
    public int readExceptionStatus(int serverAddress) throws ModbusNumberException, IOException, ModbusProtocolException {
        throw new ModbusProtocolException(ModbusExceptionCode.ILLEGAL_FUNCTION, serverAddress);
    }

    @Override
    public byte[] reportSlaveId(int serverAddress) throws ModbusProtocolException, ModbusNumberException, IOException, ModbusMasterException {
        throw new ModbusProtocolException(ModbusExceptionCode.ILLEGAL_FUNCTION, serverAddress);
    }
}
