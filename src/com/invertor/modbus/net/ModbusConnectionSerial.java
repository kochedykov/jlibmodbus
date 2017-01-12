package com.invertor.modbus.net;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.net.transport.ModbusTransport;
import com.invertor.modbus.serial.SerialPort;
import com.invertor.modbus.serial.SerialPortException;

import java.io.IOException;

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
abstract class ModbusConnectionSerial extends ModbusConnection {

    final private SerialPort serial;
    final private ModbusTransport transport;

    ModbusConnectionSerial(SerialPort serial, ModbusTransport transport) {
        this.serial = serial;
        this.transport = transport;
    }

    @Override
    public void open() throws ModbusIOException {
        try {
            this.serial.close();
            this.serial.open();
            this.serial.clear();
        } catch (SerialPortException e) {
            throw new ModbusIOException(e);
        }
    }

    @Override
    public void close() {
        this.serial.close();
    }

    @Override
    public void reset() throws ModbusIOException {
        serial.clear();
        try {
            getOutputStream().flush();
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
    }

    @Override
    public void setReadTimeout(int timeout) {
        getInputStream().setReadTimeout(timeout);
    }

    @Override
    public ModbusOutputStream getOutputStream() {
        return transport.getOutputStream();
    }

    @Override
    public ModbusInputStream getInputStream() {
        return transport.getInputStream();
    }

    @Override
    public ModbusTransport getTransport() {
        return transport;
    }
}
