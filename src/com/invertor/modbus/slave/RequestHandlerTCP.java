package com.invertor.modbus.slave;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.ModbusSlave;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.net.ModbusConnection;
import com.invertor.modbus.net.transport.ModbusTransport;

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

    RequestHandlerTCP(ModbusSlave slave, ModbusConnection conn) {
        super(slave, conn);
    }

    @Override
    public void run() {
        setListening(true);
        try {
            do {
                DataHolder dataHolder = getSlave().getDataHolder();
                ModbusTransport transport = getConn().getTransport();
                ModbusRequest request = (ModbusRequest) transport.readRequest();

                if (    //default tcp session
                        request.getServerAddress() == Modbus.TCP_DEFAULT_ID ||
                                //via gateway
                                request.getServerAddress() == getSlave().getServerAddress() ||
                                //broadcast
                                (request.getServerAddress() == Modbus.BROADCAST_ID && getSlave().isBroadcastEnabled())) {
                    ModbusMessage response = request.process(dataHolder);
                    response.setTransactionId(request.getTransactionId());
                    transport.send(response);
                }
            } while (isListening());

        } catch (ModbusNumberException e) {
            Modbus.log().warning(e.getLocalizedMessage());
        } catch (ModbusIOException e) {
            Modbus.log().warning(e.getLocalizedMessage());
        } finally {
            setListening(false);
            try {
                getConn().close();
            } catch (ModbusIOException ioe) {
                Modbus.log().warning(ioe.getMessage());
            }
        }
    }
}