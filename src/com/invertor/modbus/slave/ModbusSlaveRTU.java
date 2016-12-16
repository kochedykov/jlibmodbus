package com.invertor.modbus.slave;

import com.invertor.modbus.net.ModbusConnectionFactory;
import com.invertor.modbus.serial.SerialParameters;
import com.invertor.modbus.serial.SerialPort;
import com.invertor.modbus.serial.SerialPortException;
import com.invertor.modbus.serial.SerialUtils;

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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ModbusSlaveRTU extends ModbusSlaveSerial {

    public ModbusSlaveRTU(SerialParameters sp) throws SerialPortException {
        super(ModbusConnectionFactory.getRTU(SerialUtils.createSerial(sp)));
        final int baud_len = 1 + sp.getDataBits() + sp.getStopBits() + (sp.getParity() != SerialPort.Parity.NONE ? 1 : 0);
        final double frame_break_len = 3.5;
        int timeout = (int) Math.ceil(((double) 1000 * frame_break_len * baud_len) / sp.getBaudRate());
        /*increase timeout for get a frame certain*/
        getConn().setReadTimeout(timeout * 10);
    }

    public ModbusSlaveRTU(String device, SerialPort.BaudRate baudRate, int dataBits, int stopBits, SerialPort.Parity parity) throws SerialPortException {
        this(new SerialParameters(device, baudRate, dataBits, stopBits, parity));
    }
}
