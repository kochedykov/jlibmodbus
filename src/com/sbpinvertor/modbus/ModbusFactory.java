package com.sbpinvertor.modbus;

import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.serial.SerialPort;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

public class ModbusFactory {

    private ModbusFactory() {

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
     * @see com.sbpinvertor.modbus.serial.SerialPort.Parity
     * @see com.sbpinvertor.modbus.serial.SerialPort.BaudRate
     * @see ModbusMaster
     */
    static public ModbusMaster createModbusMasterRTU(String device, SerialPort.BaudRate baudRate, int dataBits, int stopBits, SerialPort.Parity parity) {
        return new ModbusMasterRTU(device, baudRate, dataBits, stopBits, parity);
    }

    /**
     * Creates ModbusMasterASCII instance.
     *
     * @param device   - serial port device name
     * @param baudRate - baud rate
     * @param parity   - parity bit(NONE, EVEN, ODD, MARK, SPACE)
     * @return ModbusMaster instance if there is no errors, else null
     * @see com.sbpinvertor.modbus.serial.SerialPort.Parity
     * @see com.sbpinvertor.modbus.serial.SerialPort.BaudRate
     * @see ModbusMaster
     */
    static public ModbusMaster createModbusMasterASCII(String device, SerialPort.BaudRate baudRate, SerialPort.Parity parity) {
        return new ModbusMasterASCII(device, baudRate, parity);
    }

    /**
     * Creates ModbusMasterASCII instance with even parity completion.
     *
     * @param device   - serial port device name
     * @param baudRate - baud rate
     * @return ModbusMaster instance if there is no errors, else null
     * @see com.sbpinvertor.modbus.serial.SerialPort.Parity
     * @see com.sbpinvertor.modbus.serial.SerialPort.BaudRate
     * @see ModbusMaster
     */
    static public ModbusMaster createModbusMasterASCII(String device, SerialPort.BaudRate baudRate) {
        return new ModbusMasterASCII(device, baudRate);
    }

    /**
     * Creates ModbusMasterTCP instance.
     *
     * @param host      - ip address of remote slave
     * @param keepAlive - whether or not to have socket keep alive turned on.
     * @return ModbusMaster instance if there is no errors, else null
     * @see ModbusMaster
     */
    static public ModbusMaster createModbusMasterTCP(String host, boolean keepAlive) throws ModbusTransportException {
        return createModbusMasterTCP(host, Modbus.TCP_PORT, keepAlive);
    }

    /**
     * Creates ModbusMasterTCP instance.
     *
     * @param host - ip address of remote slave
     * @return ModbusMaster instance if there is no errors, else null
     * @see ModbusMaster
     */
    static public ModbusMaster createModbusMasterTCP(String host) throws ModbusTransportException {
        return createModbusMasterTCP(host, false);
    }

    /**
     * Creates ModbusMasterTCP instance.
     *
     * @param host      - ip address of remote slave
     * @param port      - tcp port
     * @param keepAlive - whether or not to have socket keep alive turned on.
     * @return ModbusMaster instance if there is no errors, else null
     * @see ModbusMaster
     */
    static public ModbusMaster createModbusMasterTCP(String host, int port, boolean keepAlive) throws ModbusTransportException {
        return new ModbusMasterTCP(host, port, keepAlive);
    }
}
