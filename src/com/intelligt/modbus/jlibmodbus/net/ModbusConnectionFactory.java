package com.intelligt.modbus.jlibmodbus.net;

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.Socket;

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
