package com.sbpinvertor.modbus.slave;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.ModbusSlave;
import com.sbpinvertor.modbus.exception.ModbusSlaveException;
import com.sbpinvertor.modbus.msg.base.ModbusRequest;
import com.sbpinvertor.modbus.net.ModbusConnection;

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
public class RequestHandler implements Runnable {

    final private ModbusSlave slave;
    final private ModbusConnection conn;
    volatile private boolean listening = false;

    public RequestHandler(ModbusSlave slave, ModbusConnection conn) {
        this.slave = slave;
        this.conn = conn;
    }

    @Override
    public void run() {
        setListening(true);
        do {
            try {
                if (slave.getDataHolder() == null)
                    throw new ModbusSlaveException("DataHolder is null");
                ModbusRequest request = (ModbusRequest) conn.getTransport().readRequest();
                conn.getTransport().send(request.getResponse(slave.getDataHolder()));
            } catch (Exception e) {
                Modbus.log().fine("Request timeout(no clients connected)");
            }
        } while (isListening());
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }
}