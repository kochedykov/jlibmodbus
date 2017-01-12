package com.invertor.modbus.slave;

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
