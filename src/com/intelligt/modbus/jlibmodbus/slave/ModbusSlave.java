package com.intelligt.modbus.jlibmodbus.slave;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.data.DataHolderBuilder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.net.ModbusConnection;
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListenerList;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
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

abstract public class ModbusSlave implements FrameEventListenerList {
    final private List<ModbusConnection> connectionList = new ArrayList<ModbusConnection>();
    final private AtomicBoolean listening = new AtomicBoolean(false);
    final private AtomicBoolean broadcastEnabled = new AtomicBoolean(false);
    private Observable observable = new Observable() {
        @Override
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    };
    private int serverAddress = Modbus.BROADCAST_ID;
    private DataHolder dataHolder = new DataHolder();
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
        setListening(false);
        shutdownImpl();
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

    public void setDataHolder(DataHolderBuilder builder) {
        setDataHolder(builder.build());
    }

    public void setDataHolder(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
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

    protected List<ModbusConnection> getConnectionList() {
        return connectionList;
    }

    void connectionOpened(ModbusConnection connection) {
        connection.setReadTimeout(getReadTimeout());
        getConnectionList().add(connection);
    }

    void connectionClosed(ModbusConnection connection) {
        getConnectionList().remove(connection);
    }

    @Override
    public void addListener(FrameEventListener listener) {
        for (ModbusConnection c : getConnectionList()) {
            c.addListener(listener);
        }
    }

    @Override
    public void removeListener(FrameEventListener listener) {
        for (ModbusConnection c : getConnectionList()) {
            c.removeListener(listener);
        }
    }

    @Override
    public void removeListeners() {
        for (ModbusConnection c : getConnectionList()) {
            c.removeListeners();
        }
    }

    @Override
    public void fireFrameReceivedEvent(FrameEvent event) {
        for (ModbusConnection c : getConnectionList()) {
            c.fireFrameReceivedEvent(event);
        }
    }

    @Override
    public void fireFrameSentEvent(FrameEvent event) {
        for (ModbusConnection c : getConnectionList()) {
            c.fireFrameSentEvent(event);
        }
    }

    @Override
    public int countListeners() {
        int count = 0;
        for (ModbusConnection c : getConnectionList()) {
            count += c.countListeners();
        }
        return count;
    }

    /*
        facade
         */
    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    public void deleteObserver(Observer observer) {
        observable.deleteObserver(observer);
    }

    public void deleteObservers() {
        observable.deleteObservers();
    }

    public int countObservers() {
        return observable.countObservers();
    }

    protected void notifyObservers(Object o) {
        observable.notifyObservers(o);
    }
}
