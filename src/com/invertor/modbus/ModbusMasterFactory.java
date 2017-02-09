package com.invertor.modbus;

import com.invertor.modbus.master.ModbusMasterASCII;
import com.invertor.modbus.master.ModbusMasterRTU;
import com.invertor.modbus.master.ModbusMasterTCP;
import com.invertor.modbus.serial.SerialParameters;
import com.invertor.modbus.serial.SerialPortException;
import com.invertor.modbus.tcp.TcpParameters;

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

final public class ModbusMasterFactory {

    private ModbusMasterFactory() {

    }

    /**
     * Creates a ModbusMasterRTU instance.
     *
     * @param sp parameters of the serial port you would like to use.
     * @return the newly created rtu-master
     * @see com.invertor.modbus.serial.SerialPort.Parity
     * @see com.invertor.modbus.serial.SerialPort.BaudRate
     * @see com.invertor.modbus.ModbusMaster
     * @see com.invertor.modbus.master.ModbusMasterRTU
     * @see com.invertor.modbus.serial.SerialParameters
     */
    static public ModbusMaster createModbusMasterRTU(SerialParameters sp) throws SerialPortException {
        return new ModbusMasterRTU(sp);
    }

    /**
     * Creates a ModbusMasterASCII instance.
     *
     * @param sp parameters of the serial port you would like to use.
     * @return the newly created ascii-master
     * @see com.invertor.modbus.serial.SerialPort.Parity
     * @see com.invertor.modbus.serial.SerialPort.BaudRate
     * @see com.invertor.modbus.ModbusMaster
     * @see com.invertor.modbus.master.ModbusMasterASCII
     */
    static public ModbusMaster createModbusMasterASCII(SerialParameters sp) throws SerialPortException {
        return new ModbusMasterASCII(sp);
    }

    /**
     * Creates ModbusMasterTCP instance.
     *
     * @param tcpParameters - a TcpParameters instance
     * @return the newly created tcp-master
     * @see com.invertor.modbus.ModbusMaster
     * @see com.invertor.modbus.master.ModbusMasterTCP
     * @see com.invertor.modbus.tcp.TcpParameters
     */
    static public ModbusMaster createModbusMasterTCP(TcpParameters tcpParameters) {
        return new ModbusMasterTCP(tcpParameters);
    }
}
