package com.invertor.modbus.net;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.serial.SerialPort;
import com.invertor.modbus.tcp.TcpParameters;

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

public class ModbusConnectionFactory {
    static public ModbusConnection getASCII(SerialPort serial) {
        return new ModbusConnectionASCII(serial);
    }

    static public ModbusConnection getRTU(SerialPort serial) {
        return new ModbusConnectionRTU(serial);
    }

    static public ModbusConnection getTcpMaster(TcpParameters tcpParameters) {
        return new ModbusMasterConnectionTCP(tcpParameters);
    }

    static public ModbusConnection getTcpSlave(Socket socket) throws ModbusIOException {
        return new ModbusSlaveConnectionTCP(socket);
    }
}
