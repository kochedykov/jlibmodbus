package com.intelligt.modbus.jlibmodbus.slave;

import com.intelligt.modbus.jlibmodbus.net.ModbusConnection;

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
abstract public class RequestHandler implements Runnable {

    final private ModbusSlave slave;
    final private ModbusConnection conn;
    volatile private boolean listening = false;

    public RequestHandler(ModbusSlave slave, ModbusConnection conn) {
        this.slave = slave;
        this.conn = conn;
    }

    public boolean isListening() {
        return listening;
    }

    protected void setListening(boolean listening) {
        this.listening = listening;
    }

    abstract public void closeConnection();

    public ModbusSlave getSlave() {
        return slave;
    }

    public ModbusConnection getConnection() {
        return conn;
    }
}
