package com.intelligt.modbus.jlibmodbus.slave;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.CommStatus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.data.comm.ModbusCommEventSend;
import com.intelligt.modbus.jlibmodbus.exception.ModbusChecksumException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.request.GetCommEventCounterRequest;
import com.intelligt.modbus.jlibmodbus.msg.request.GetCommEventLogRequest;
import com.intelligt.modbus.jlibmodbus.net.ModbusConnection;
import com.intelligt.modbus.jlibmodbus.net.transport.ModbusTransport;

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
class RequestHandlerSerial extends RequestHandler {

    RequestHandlerSerial(ModbusSlave slave, ModbusConnection conn) {
        super(slave, conn);
    }

    @Override
    public void closeConnection() {
        setListening(false);
    }

    @Override
    public void run() {
        try {
            getConnection().open();
            getSlave().connectionOpened(getConnection());
            setListening(true);
            do {
                DataHolder dataHolder = getSlave().getDataHolder();
                CommStatus commStatus = dataHolder.getCommStatus();
                ModbusTransport transport = getConnection().getTransport();
                try {
                    ModbusRequest request = (ModbusRequest) transport.readRequest();

                    commStatus.incBusMessageCounter();
                    if (!(request instanceof GetCommEventCounterRequest ||
                            request instanceof GetCommEventLogRequest)) {
                        commStatus.enter();
                    }
                    if (request.getServerAddress() == getSlave().getServerAddress()) {
                        try {
                            ModbusResponse response = request.process(dataHolder);
                            commStatus.incSlaveMessageCounter();
                            if (response.isException()) {
                                commStatus.addEvent(ModbusCommEventSend.createExceptionSentRead());
                                commStatus.incExErrorCounter();
                            } else {
                                if (!(request instanceof GetCommEventCounterRequest ||
                                        request instanceof GetCommEventLogRequest)) {
                                    commStatus.incEventCounter();
                                }
                            }
                            if (!commStatus.isListenOnlyMode())
                                transport.send(response);
                            if (commStatus.isRestartCommunicationsOption()) {
                                commStatus.restartCommunicationsOption();
                                getSlave().shutdown();
                                getSlave().listen();
                            }
                        } catch (RuntimeException re) {
                            throw re;
                        } catch (Exception e) {
                            /*
                             * quantity of messages addressed to the remote device for
                             * which it has returned no response (neither a normal response nor an exception response)
                             */
                            commStatus.incNoResponseCounter();
                            throw e;
                        }
                    } else if (/*broadcast*/ request.getServerAddress() == Modbus.BROADCAST_ID && getSlave().isBroadcastEnabled()) {
                        //we do not answer these requests to avoid collisions on the bus
                        request.process(dataHolder);
                    }
                } catch (ModbusChecksumException e) {
                    commStatus.incCommErrorCounter();
                } catch (Exception e) {
                    Modbus.log().warning(e.getLocalizedMessage());
                    //e.printStackTrace();
                } finally {
                    commStatus.leave();
                }
            } while (isListening());

            if (getConnection().isOpened()) {
                getConnection().close();
                getSlave().connectionClosed(getConnection());
            }
        } catch (ModbusIOException e) {
            Modbus.log().warning(e.getMessage());
        }
    }
}