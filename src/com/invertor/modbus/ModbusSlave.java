package com.invertor.modbus;

import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.data.DataHolderBuilder;
import com.invertor.modbus.exception.ModbusIOException;

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

abstract public class ModbusSlave {

    private int serverAddress = 1;
    private DataHolder dataHolder = new DataHolder();

    protected ModbusSlave() {
    }

    /**
     * starts the ModbusSlave thread.
     */
    abstract public void listen() throws ModbusIOException;

    /**
     * should have stop the thread of the ModbusSlave.
     */
    abstract public void shutdown() throws ModbusIOException;


    /*Getters & Setters*/
    public DataHolder getDataHolder() {
        return dataHolder;
    }

    public void setDataHolder(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    public void setDataHolder(DataHolderBuilder builder) {
        setDataHolder(builder.build());
    }

    public int getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(int serverAddress) {
        if (Modbus.checkServerAddress(serverAddress))
            this.serverAddress = serverAddress;
    }
}
