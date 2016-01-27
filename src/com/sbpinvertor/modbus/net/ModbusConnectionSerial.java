package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.exception.ModbusTransportException;
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

    public ModbusConnectionSerial(SerialPort serial) {
        this.serial = serial;
    }

    @Override
    public void open() throws ModbusTransportException {
        try {
            this.serial.close();
            this.serial.open();
            this.serial.clear();
        } catch (SerialPortException e) {
            throw new ModbusTransportException(e);
        }
    }

    @Override
    public void close() throws ModbusTransportException {
        this.serial.close();
    }

    @Override
    public void reset() throws ModbusTransportException {
        serial.clear();
    }
}
