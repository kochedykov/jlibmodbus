package com.intelligt.modbus.jlibmodbus.slave;

import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

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

final public class ModbusSlaveFactory {

    private ModbusSlaveFactory() {

    }

    /**
     * Creates a ModbusSlaveRTU instance.
     *
     * @param sp parameters of the serial port
     * @return the newly created rtu-slave
     * @see SerialParameters
     * @see ModbusSlaveRTU
     * @see ModbusSlave
     */
    static public ModbusSlave createModbusSlaveRTU(SerialParameters sp) throws SerialPortException {
        return new ModbusSlaveRTU(sp);
    }

    /**
     * Creates a ModbusSlaveASCII instance.
     *
     * @param sp parameters of the serial port
     * @return the newly created ascii-slave
     * @see SerialParameters
     * @see ModbusSlaveASCII
     * @see ModbusSlave
     */
    static public ModbusSlave createModbusSlaveASCII(SerialParameters sp) throws SerialPortException {
        return new ModbusSlaveASCII(sp);
    }

    /**
     * Creates a ModbusSlaveTCP instance.
     *
     * @param tcpParameters tcp parameters.
     * @return the newly created tcp-slave
     * @see ModbusSlaveTCP
     * @see ModbusSlave
     * @see TcpParameters
     */
    static public ModbusSlave createModbusSlaveTCP(TcpParameters tcpParameters) {
        return new ModbusSlaveTCP(tcpParameters);
    }

    /**
     * Creates a ModbusSlaveTCP instance.
     *
     * @param tcpParameters tcp parameters.
     * @param poolSize      the number of threads in the pool
     * @return the newly created tcp-slave
     * @see ModbusSlaveTCP
     * @see ModbusSlave
     * @see TcpParameters
     */
    static public ModbusSlave createModbusSlaveTCP(TcpParameters tcpParameters, int poolSize) {
        return new ModbusSlaveTCP(tcpParameters, poolSize);
    }
}
