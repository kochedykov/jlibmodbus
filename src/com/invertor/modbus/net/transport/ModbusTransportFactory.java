package com.invertor.modbus.net.transport;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.serial.SerialPort;

import java.net.Socket;

/**
 * Copyright (c) 2015-2016 JSC Invertor
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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

public class ModbusTransportFactory {
    static public ModbusTransport createRTU(SerialPort serial) {
        return new ModbusTransportRTU(serial);
    }

    static public ModbusTransport createASCII(SerialPort serial) {
        return new ModbusTransportASCII(serial);
    }

    static public ModbusTransport createTCP(Socket socket) {
        try {
            return new ModbusTransportTCP(socket);
        } catch (ModbusIOException e) {
            Modbus.log().severe(e.getLocalizedMessage());
            return null;
        }
    }
}
