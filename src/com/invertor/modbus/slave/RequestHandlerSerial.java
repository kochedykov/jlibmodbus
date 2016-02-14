package com.invertor.modbus.slave;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.ModbusSlave;
import com.invertor.modbus.data.CommStatus;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.net.ModbusConnection;
import com.invertor.modbus.net.ModbusTransport;

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
public class RequestHandlerSerial extends RequestHandler {

    public RequestHandlerSerial(ModbusSlave slave, ModbusConnection conn) {
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
                commStatus.enter();
                if (request.getServerAddress() == getSlave().getServerAddress()) {
                    ModbusResponse response = request.getResponse(dataHolder);
                    commStatus.incrementMessageCounter();
                    if (!response.isException())
                        commStatus.incrementEventCounter();
                    transport.send(response);
                }
            } catch (ModbusIOException e) {
                Modbus.log().fine("Request timeout(no clients connected)");
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