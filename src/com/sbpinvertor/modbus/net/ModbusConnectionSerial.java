package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.exception.ModbusIOException;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.net.stream.base.ModbusOutputStream;
import com.sbpinvertor.modbus.serial.SerialPort;
import com.sbpinvertor.modbus.serial.SerialPortException;

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
abstract public class ModbusConnectionSerial extends ModbusConnection {

    final private SerialPort serial;
    final private ModbusTransport transport;

    public ModbusConnectionSerial(SerialPort serial, ModbusTransport transport) {
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
    public void reset() {
        serial.clear();
        getInputStream().reset();
        getOutputStream().reset();
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
