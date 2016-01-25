package com.sbpinvertor.modbus;

import com.sbpinvertor.conn.SerialPort;
import com.sbpinvertor.conn.SerialPortException;
import com.sbpinvertor.modbus.net.ModbusTransportASCII;

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

class ModbusMasterASCII extends ModbusMaster {

    public ModbusMasterASCII(String device, SerialPort.BaudRate baudRate, int dataBits, int stopBits, SerialPort.Parity parity) throws SerialPortException {
        super(new ModbusTransportASCII(new SerialPort(device, baudRate, dataBits, stopBits, parity)));
    }
}
