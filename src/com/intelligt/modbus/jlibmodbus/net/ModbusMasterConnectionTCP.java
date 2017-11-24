package com.intelligt.modbus.jlibmodbus.net;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.net.transport.ModbusTransport;
import com.intelligt.modbus.jlibmodbus.net.transport.ModbusTransportFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

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
class ModbusMasterConnectionTCP extends ModbusConnection {

    final private TcpParameters parameters;
    private ModbusTransport transport = null;

    ModbusMasterConnectionTCP(TcpParameters parameters) {
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
    public void open() throws ModbusIOException {
        if (isNotOpened()) {
            if (parameters != null) {
                Socket socket = new Socket();
                InetSocketAddress isa = new InetSocketAddress(parameters.getHost(), parameters.getPort());
                try {
                    socket.connect(isa, Modbus.MAX_CONNECTION_TIMEOUT);
                    socket.setKeepAlive(parameters.isKeepAlive());
                } catch (Exception e) {
                    throw new ModbusIOException(e);
                }
                transport = ModbusTransportFactory.createTCP(socket);
                setReadTimeout(getReadTimeout());
                setOpened(true);
            }
        }
    }

    @Override
    public void close() throws ModbusIOException {
        setOpened(false);
        try {
            if (transport != null) {
                transport.close();
            }
        } catch (IOException e) {
            throw new ModbusIOException(e);
        } finally {
            transport = null;
        }
    }
}
