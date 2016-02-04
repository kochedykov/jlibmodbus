package com.sbpinvertor.modbus.slave;

import com.sbpinvertor.modbus.ModbusSlave;
import com.sbpinvertor.modbus.net.ModbusConnection;
import com.sbpinvertor.modbus.net.ModbusConnectionSerial;

import java.io.IOException;

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
public class ModbusSlaveSerial extends ModbusSlave {

    final private ModbusConnection conn;
    final private RequestHandler requestHandler;
    private Thread thread = null;

    public ModbusSlaveSerial(ModbusConnectionSerial conn) {
        this.conn = conn;
        this.requestHandler = new RequestHandler(this, conn);
    }

    @Override
    public void open() throws IOException {
        close();
        conn.open();
        thread = new Thread(requestHandler);
        thread.start();
    }

    @Override
    public void close() throws IOException {
        requestHandler.setListening(false);
        try {
            if (thread != null)
                thread.join(1000);
            thread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        conn.close();
    }

    public ModbusConnection getConn() {
        return conn;
    }
}
