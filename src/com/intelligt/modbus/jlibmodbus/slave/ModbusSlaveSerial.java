package com.intelligt.modbus.jlibmodbus.slave;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.net.ModbusConnection;
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener;
import com.intelligt.modbus.jlibmodbus.utils.SerialPortInfo;

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
    final private SerialPortInfo serialPortInfo;
    private Thread mainThread = null;

    ModbusSlaveSerial(SerialParameters serialParameters, ModbusConnection conn) {
        this.conn = conn;
        this.serialPortInfo = new SerialPortInfo(serialParameters);
        this.requestHandler = new RequestHandlerSerial(this, conn);
        getConnection().setReadTimeout(Modbus.DEFAULT_READ_TIMEOUT);
    }

    void closeConnection() throws ModbusIOException {
        getConnection().close();
        connectionClosed(getConnection());
        serialPortInfo.setOpened(false);
    }

    void openConnection() throws ModbusIOException {
        getConnection().open();
        connectionOpened(getConnection());
        serialPortInfo.setOpened(true);
    }

    @Override
    synchronized public void listenImpl() throws ModbusIOException {
        shutdown();
        openConnection();
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
        closeConnection();
    }

    protected ModbusConnection getConnection() {
        return conn;
    }

    @Override
    void connectionOpened(ModbusConnection connection) {
        super.connectionOpened(connection);
        notifyObservers(serialPortInfo);
    }

    @Override
    void connectionClosed(ModbusConnection connection) {
        super.connectionClosed(connection);
        notifyObservers(serialPortInfo);
    }

    @Override
    public void addListener(FrameEventListener listener) {
        getConnection().addListener(listener);
    }

    @Override
    public void removeListener(FrameEventListener listener) {
        getConnection().removeListener(listener);
    }

    @Override
    public void removeListeners() {
        getConnection().removeListeners();
    }

    @Override
    public void fireFrameReceivedEvent(FrameEvent event) {
        getConnection().fireFrameReceivedEvent(event);
    }

    @Override
    public void fireFrameSentEvent(FrameEvent event) {
        fireFrameSentEvent(event);
    }
}
