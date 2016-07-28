package com.invertor.modbus.slave;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.ModbusSlave;
import com.invertor.modbus.data.CommStatus;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.data.events.ModbusEventSend;
import com.invertor.modbus.exception.ModbusChecksumException;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.request.GetCommEventCounterRequest;
import com.invertor.modbus.msg.request.GetCommEventLogRequest;
import com.invertor.modbus.net.ModbusConnection;
import com.invertor.modbus.net.transport.ModbusTransport;

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
class RequestHandlerSerial extends RequestHandler {

    RequestHandlerSerial(ModbusSlave slave, ModbusConnection conn) {
        super(slave, conn);
    }

    @Override
    public void run() {
        setListening(true);
        do {
            DataHolder dataHolder = getSlave().getDataHolder();
            CommStatus commStatus = dataHolder.getCommStatus();
            ModbusTransport transport = getConn().getTransport();
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
                            commStatus.addEvent(ModbusEventSend.createExceptionSentRead());
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
                            getSlave().open();
                        }
                    } catch (Exception e) {
                        /*
                         * quantity of messages addressed to the remote device for
                         * which it has returned no response (neither a normal response nor an exception response)
                         */
                        commStatus.incNoResponseCounter();
                        throw e;
                    }
                }
            } catch (ModbusChecksumException e) {
                commStatus.incCommErrorCounter();
            } catch (Exception e) {
                Modbus.log().warning(e.getLocalizedMessage());
            } finally {
                commStatus.leave();
            }
        } while (isListening());
        try {
            getConn().close();
        } catch (ModbusIOException e) {
            Modbus.log().warning(e.getMessage());
        }
    }
}