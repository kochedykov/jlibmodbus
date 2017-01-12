package com.invertor.modbus;

import com.invertor.modbus.master.ModbusMasterASCII;
import com.invertor.modbus.master.ModbusMasterRTU;
import com.invertor.modbus.master.ModbusMasterTCP;
import com.invertor.modbus.serial.SerialParameters;
import com.invertor.modbus.serial.SerialPort;
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
     * Creates ModbusMasterRTU instance.
     *
     * @param device   - serial port device name
     * @param baudRate - baud rate
     * @param dataBits - data bit count
     * @param stopBits - stop bit count(1,2)
     * @param parity   - parity bit(NONE, EVEN, ODD, MARK, SPACE)
     * @return ModbusMaster instance if there is no errors, else null
     * @see com.invertor.modbus.serial.SerialPort.Parity
     * @see com.invertor.modbus.serial.SerialPort.BaudRate
     * @see com.invertor.modbus.ModbusMaster
     * @see com.invertor.modbus.master.ModbusMasterRTU
     */
    static public ModbusMaster createModbusMasterRTU(String device, SerialPort.BaudRate baudRate, int dataBits, int stopBits, SerialPort.Parity parity) throws SerialPortException {
        return new ModbusMasterRTU(device, baudRate, dataBits, stopBits, parity);
    }

    /**
     * Creates ModbusMasterRTU instance.
     *
     * @param sp - a SerialParameters instance.
     * @return ModbusMaster instance if there is no errors, else null
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
     * Creates ModbusMasterASCII instance.
     *
     * @param device   - serial port device name
     * @param baudRate - baud rate
     * @param parity   - parity bit(NONE, EVEN, ODD, MARK, SPACE)
     * @return ModbusMaster instance if there is no errors, else null
     * @see com.invertor.modbus.serial.SerialPort.Parity
     * @see com.invertor.modbus.serial.SerialPort.BaudRate
     * @see com.invertor.modbus.ModbusMaster
     * @see com.invertor.modbus.master.ModbusMasterASCII
     */
    static public ModbusMaster createModbusMasterASCII(String device, SerialPort.BaudRate baudRate, SerialPort.Parity parity) throws SerialPortException {
        return new ModbusMasterASCII(device, baudRate, parity);
    }

    /**
     * Creates ModbusMasterASCII instance with even parity completion.
     *
     * @param device   - serial port device name
     * @param baudRate - baud rate
     * @return ModbusMaster instance if there is no errors, else null
     * @see com.invertor.modbus.serial.SerialPort.Parity
     * @see com.invertor.modbus.serial.SerialPort.BaudRate
     * @see com.invertor.modbus.ModbusMaster
     * @see com.invertor.modbus.master.ModbusMasterASCII
     */
    static public ModbusMaster createModbusMasterASCII(String device, SerialPort.BaudRate baudRate) throws SerialPortException {
        return new ModbusMasterASCII(device, baudRate);
    }

    /**
     * Creates ModbusMasterTCP instance.
     *
     * @param host      - ip address of remote slave
     * @param keepAlive - whether or not to have socket keep alive turned on.
     * @return ModbusMaster instance if there is no errors, else null
     * @see com.invertor.modbus.ModbusMaster
     * @see com.invertor.modbus.master.ModbusMasterTCP
     */
    static public ModbusMaster createModbusMasterTCP(String host, boolean keepAlive) {
        return createModbusMasterTCP(host, Modbus.TCP_PORT, keepAlive);
    }

    /**
     * Creates ModbusMasterTCP instance.
     *
     * @param host - ip address of remote slave
     * @return ModbusMaster instance if there is no errors, else null
     * @see com.invertor.modbus.ModbusMaster
     * @see com.invertor.modbus.master.ModbusMasterTCP
     */
    static public ModbusMaster createModbusMasterTCP(String host) {
        return createModbusMasterTCP(host, false);
    }

    /**
     * Creates ModbusMasterTCP instance.
     *
     * @param host      - ip address of remote slave
     * @param port      - tcp port
     * @param keepAlive - whether or not to have socket keep alive turned on.
     * @return ModbusMaster instance if there is no errors, else null
     * @see com.invertor.modbus.ModbusMaster
     * @see com.invertor.modbus.master.ModbusMasterTCP
     */
    static public ModbusMaster createModbusMasterTCP(String host, int port, boolean keepAlive) {
        return new ModbusMasterTCP(new TcpParameters(host, port, keepAlive));
    }

    /**
     * Creates ModbusMasterTCP instance.
     *
     * @param tcpParameters - a TcpParameters instance
     * @return ModbusMaster instance if there is no errors, else null
     * @see com.invertor.modbus.ModbusMaster
     * @see com.invertor.modbus.master.ModbusMasterTCP
     * @see com.invertor.modbus.tcp.TcpParameters
     */
    static public ModbusMaster createModbusMasterTCP(TcpParameters tcpParameters) {
        return new ModbusMasterTCP(tcpParameters);
    }
}
