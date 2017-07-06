package com.invertor.modbus;

import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.data.DataHolderBuilder;
import com.invertor.modbus.exception.ModbusIOException;

import java.util.concurrent.atomic.AtomicBoolean;

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
    private AtomicBoolean listening = new AtomicBoolean(false);
    private AtomicBoolean broadcastEnabled = new AtomicBoolean(false);
    /**
     * a timeout for single connection handler. if master makes a new connection for every data request,
     * we should close it's last connection as soon as possible. Else, if master is working through a single connection,
     * we should be waiting for the next request as long as it is possible :).
     */
    private volatile int readTimeout = Modbus.MAX_RESPONSE_TIMEOUT;

    protected ModbusSlave() {
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    abstract protected void listenImpl() throws ModbusIOException;

    abstract protected void shutdownImpl() throws ModbusIOException;

    /**
     * starts the ModbusSlave thread.
     */
    final public void listen() throws ModbusIOException {
        listenImpl();
        setListening(true);
    }

    /**
     * should have stop the thread of the ModbusSlave.
     */
    final public void shutdown() throws ModbusIOException {
        shutdownImpl();
        setListening(false);
    }

    public boolean isListening() {
        return listening.get();
    }

    protected void setListening(boolean listening) {
        this.listening.set(listening);
    }

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

    public boolean isBroadcastEnabled() {
        return broadcastEnabled.get();
    }

    public void setBroadcastEnabled(boolean broadcastEnabled) {
        this.broadcastEnabled.set(broadcastEnabled);
    }
}
