package com.intelligt.modbus.jlibmodbus.slave;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusMessage;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.net.ModbusConnectionFactory;
import com.intelligt.modbus.jlibmodbus.net.transport.ModbusTransport;

import java.io.IOException;
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
class RequestHandlerTCP extends RequestHandler {

    final private Socket socket;

    RequestHandlerTCP(ModbusSlaveTCP slave, Socket s) throws ModbusIOException {
        super(slave, ModbusConnectionFactory.getTcpSlave(s));
        socket = s;
    }

    @Override
    public void run() {
        setListening(true);
        try {
            getSlave().connectionOpened(getConnection());
            do {
                try {
                    DataHolder dataHolder = getSlave().getDataHolder();
                    ModbusTransport transport = getConnection().getTransport();
                    ModbusRequest request = (ModbusRequest) transport.readRequest();

                    if (/*default tcp session*/request.getServerAddress() == Modbus.TCP_DEFAULT_ID ||
                            /*gateway*/request.getServerAddress() == getSlave().getServerAddress()) {
                        ModbusMessage response = request.process(dataHolder);
                        response.setTransactionId(request.getTransactionId());
                        if (request.getServerAddress() != Modbus.BROADCAST_ID)
                            transport.send(response);
                    } else if (/*broadcast*/ request.getServerAddress() == Modbus.BROADCAST_ID && getSlave().isBroadcastEnabled()) {
                        //we do not answer broadcast requests
                        request.process(dataHolder);
                    }
                } catch (ModbusNumberException e) {
                    Modbus.log().warning(e.getLocalizedMessage());
                }
            } while (isListening());
        } catch (ModbusIOException e) {
            if (getSlave().isListening()) {
                Modbus.log().warning(e.getLocalizedMessage());
            }
        } finally {
            setListening(false);
            try {
                if (getConnection().isOpened()) {
                    getConnection().close();
                    getSlave().connectionClosed(getConnection());
                }
            } catch (ModbusIOException ioe) {
                Modbus.log().warning(ioe.getMessage());
            }
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (socket.isConnected() && !isListening()) {
                socket.close();
                setListening(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}