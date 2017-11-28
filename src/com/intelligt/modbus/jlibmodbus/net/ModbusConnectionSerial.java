package com.intelligt.modbus.jlibmodbus.net;

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingOutputStream;
import com.intelligt.modbus.jlibmodbus.net.transport.ModbusTransport;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException;

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

/**
 * this class is an extension of the ModbusConnection class
 * to implement features for all serial port based connections.
 *
 * @see ModbusConnectionRTU
 * @see ModbusConnectionASCII
 * @see ModbusConnection
 */
abstract class ModbusConnectionSerial extends ModbusConnection {

    /**
     * instance of SerialPort class,
     * which is a wrapper for concrete serial API implementation.
     *
     * @see SerialPort
     */
    final private SerialPort serial;
    final private ModbusTransport transport;

    ModbusConnectionSerial(SerialPort serial, ModbusTransport transport) {
        this.serial = serial;
        this.transport = transport;
    }

    @Override
    protected void openImpl() throws ModbusIOException {
        if (!isOpened()) {
            try {
                this.serial.open();
            } catch (SerialPortException e) {
                throw new ModbusIOException(e);
            }
        }
    }

    @Override
    protected void closeImpl() {
        this.serial.close();
    }

    @Override
    public LoggingOutputStream getOutputStream() {
        return transport.getOutputStream();
    }

    @Override
    public LoggingInputStream getInputStream() {
        return transport.getInputStream();
    }

    @Override
    public ModbusTransport getTransport() {
        return transport;
    }
}
