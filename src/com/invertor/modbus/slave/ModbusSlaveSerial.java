package com.invertor.modbus.slave;

import com.invertor.modbus.ModbusSlave;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.net.ModbusConnection;

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
class ModbusSlaveSerial extends ModbusSlave {

    final private ModbusConnection conn;
    final private RequestHandler requestHandler;
    private Thread mainThread = null;

    ModbusSlaveSerial(ModbusConnection conn) {
        this.conn = conn;
        this.requestHandler = new RequestHandlerSerial(this, conn);
    }

    @Override
    synchronized public void listenImpl() throws ModbusIOException {
        shutdown();
        conn.open();
        mainThread = new Thread(requestHandler);
        mainThread.start();
    }

    @Override
    synchronized public void shutdownImpl() throws ModbusIOException {
        requestHandler.setListening(false);
        try {
            if (mainThread != null) {
                mainThread.join(2000);
                if (mainThread.isAlive())
                    mainThread.interrupt();
            }
            mainThread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        conn.close();
    }

    ModbusConnection getConn() {
        return conn;
    }
}
