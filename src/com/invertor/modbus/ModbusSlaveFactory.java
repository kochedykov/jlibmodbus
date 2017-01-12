package com.invertor.modbus;

import com.invertor.modbus.serial.SerialParameters;
import com.invertor.modbus.serial.SerialPort;
import com.invertor.modbus.serial.SerialPortException;
import com.invertor.modbus.slave.ModbusSlaveASCII;
import com.invertor.modbus.slave.ModbusSlaveRTU;
import com.invertor.modbus.slave.ModbusSlaveTCP;
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

final public class ModbusSlaveFactory {

    private ModbusSlaveFactory() {

    }

    /**
     * Creates ModbusSlaveRTU instance.
     *
     * @param device   - serial port device name
     * @param baudRate - baud rate
     * @param dataBits - data bit count
     * @param stopBits - stop bit count(1,2)
     * @param parity   - parity bit(NONE, EVEN, ODD, MARK, SPACE)
     * @return ModbusSlave instance if there is no errors, else null
     * @see com.invertor.modbus.serial.SerialPort.Parity
     * @see com.invertor.modbus.serial.SerialPort.BaudRate
     * @see com.invertor.modbus.slave.ModbusSlaveRTU
     * @see com.invertor.modbus.ModbusSlave
     */
    static public ModbusSlave createModbusSlaveRTU(String device, SerialPort.BaudRate baudRate, int dataBits, int stopBits, SerialPort.Parity parity) throws SerialPortException {
        return new ModbusSlaveRTU(device, baudRate, dataBits, stopBits, parity);
    }

    /**
     * Creates ModbusSlaveRTU instance.
     *
     * @param sp - a SerialParameters instance.
     * @return ModbusSlave instance if there is no errors, else null
     * @see com.invertor.modbus.serial.SerialParameters
     * @see com.invertor.modbus.slave.ModbusSlaveRTU
     * @see com.invertor.modbus.ModbusSlave
     */
    static public ModbusSlave createModbusSlaveRTU(SerialParameters sp) throws SerialPortException {
        return new ModbusSlaveRTU(sp);
    }

    /**
     * Creates ModbusSlaveASCII instance.
     *
     * @param device   - serial port device name
     * @param baudRate - baud rate
     * @param parity   - parity bit(NONE, EVEN, ODD, MARK, SPACE)
     * @return ModbusSlave instance if there is no errors, else null
     * @see com.invertor.modbus.serial.SerialParameters
     * @see com.invertor.modbus.slave.ModbusSlaveASCII
     * @see com.invertor.modbus.ModbusSlave
     */
    static public ModbusSlave createModbusSlaveASCII(String device, SerialPort.BaudRate baudRate, SerialPort.Parity parity) throws SerialPortException {
        return new ModbusSlaveASCII(device, baudRate, parity);
    }

    /**
     * Creates ModbusSlaveASCII instance with even parity completion.
     *
     * @param device   - serial port device name
     * @param baudRate - baud rate
     * @return ModbusSlave instance if there is no errors, else null
     * @see com.invertor.modbus.serial.SerialPort.BaudRate
     * @see com.invertor.modbus.slave.ModbusSlaveASCII
     * @see com.invertor.modbus.ModbusSlave
     */
    static public ModbusSlave createModbusSlaveASCII(String device, SerialPort.BaudRate baudRate) throws SerialPortException {
        return new ModbusSlaveASCII(device, baudRate);
    }

    /**
     * Creates ModbusSlaveTCP instance.
     *
     * @param host - ip address of remote slave
     * @return ModbusSlave instance if there is no errors, else null
     * @see com.invertor.modbus.slave.ModbusSlaveTCP
     * @see com.invertor.modbus.ModbusSlave
     */
    static public ModbusSlave createModbusSlaveTCP(String host) {
        return createModbusSlaveTCP(host, Modbus.TCP_PORT);
    }

    /**
     * Creates ModbusSlaveTCP instance.
     *
     * @param host - ip address of remote slave
     * @param port - tcp port
     * @return ModbusSlave instance if there is no errors, else null
     * @see com.invertor.modbus.slave.ModbusSlaveTCP
     * @see com.invertor.modbus.ModbusSlave
     */
    static public ModbusSlave createModbusSlaveTCP(String host, int port) {
        return new ModbusSlaveTCP(new TcpParameters(host, port, false));
    }

    /**
     * Creates ModbusSlaveTCP instance.
     *
     * @param tcpParameters - a TcpParameters instance
     * @return ModbusSlave instance if there is no errors, else null
     * @see com.invertor.modbus.slave.ModbusSlaveTCP
     * @see com.invertor.modbus.ModbusSlave
     * @see com.invertor.modbus.tcp.TcpParameters
     */
    static public ModbusSlave createModbusSlaveTCP(TcpParameters tcpParameters) {
        return new ModbusSlaveTCP(tcpParameters);
    }
}
