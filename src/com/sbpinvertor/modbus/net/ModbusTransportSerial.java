package com.sbpinvertor.modbus.net;

import com.sbpinvertor.conn.SerialPort;
import com.sbpinvertor.conn.SerialPortException;
import com.sbpinvertor.modbus.data.ModbusInputStream;
import com.sbpinvertor.modbus.data.ModbusOutputStream;
import com.sbpinvertor.modbus.data.base.ModbusMessage;
import com.sbpinvertor.modbus.data.base.ModbusResponse;
import com.sbpinvertor.modbus.exception.ModbusTransportException;

import java.io.IOException;

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
abstract public class ModbusTransportSerial extends ModbusTransport {

    final private SerialPort serial;

    public ModbusTransportSerial(SerialPort serial) throws SerialPortException {
        this.serial = serial;
        serial.open();
    }

    @Override
    public ModbusResponse sendRequest(ModbusMessage msg) throws ModbusTransportException {
        ModbusResponse response;
        try {
            serial.clear();
            checksumInit();
            response = super.sendRequest(msg);
            if (!checksumValid())
                throw new ModbusTransportException("control sum check failed.");
        } catch (IOException e) {
            throw new ModbusTransportException(e);
        }
        return response;
    }

    @Override
    protected void finalize()
            throws Throwable {
        serial.close();
    }

    abstract void checksumInit();
    abstract boolean checksumValid() throws IOException;
}
