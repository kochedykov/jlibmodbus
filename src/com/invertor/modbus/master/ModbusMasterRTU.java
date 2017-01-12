package com.invertor.modbus.master;

import com.invertor.modbus.net.ModbusConnection;
import com.invertor.modbus.net.ModbusConnectionFactory;
import com.invertor.modbus.serial.SerialParameters;
import com.invertor.modbus.serial.SerialPort;
import com.invertor.modbus.serial.SerialPortException;
import com.invertor.modbus.serial.SerialUtils;

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

final public class ModbusMasterRTU extends ModbusMasterSerial {

    final private ModbusConnection conn;

    public ModbusMasterRTU(SerialParameters parameters) throws SerialPortException {
        conn = ModbusConnectionFactory.getRTU(SerialUtils.createSerial(parameters));
    }

    public ModbusMasterRTU(String device, SerialPort.BaudRate baudRate, int dataBits, int stopBits, SerialPort.Parity parity) throws SerialPortException {
        this(new SerialParameters(device, baudRate, dataBits, stopBits, parity));
    }

    @Override
    protected ModbusConnection getConnection() {
        return conn;
    }
}
