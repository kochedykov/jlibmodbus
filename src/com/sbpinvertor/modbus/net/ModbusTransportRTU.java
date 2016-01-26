package com.sbpinvertor.modbus.net;

import com.sbpinvertor.conn.SerialPort;
import com.sbpinvertor.conn.SerialPortException;
import com.sbpinvertor.modbus.net.streaming.InputStreamRTU;
import com.sbpinvertor.modbus.net.streaming.OutputStreamRTU;
import com.sbpinvertor.modbus.net.streaming.base.ModbusInputStream;
import com.sbpinvertor.modbus.net.streaming.base.ModbusOutputStream;

import java.io.IOException;

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
public class ModbusTransportRTU extends ModbusTransportSerial {
    final private OutputStreamRTU os;
    final private InputStreamRTU is;

    public ModbusTransportRTU(SerialPort serial) throws SerialPortException {
        super(serial);
        os = new OutputStreamRTU(serial);
        is = new InputStreamRTU(serial, this);
    }

    @Override
    void checksumInit() {
        os.reset();
        is.reset();
    }

    @Override
    boolean checksumValid() {
        try {
            //read the crc part
            int crc = is.readShortLE();
            // hook with crc
            return crc != 0 && is.getCrc() == 0;
        } catch (IOException e) {
            return false;
        }
    }

    public ModbusOutputStream getOutputStream() {
        return os;
    }

    public ModbusInputStream getInputStream() {
        return is;
    }
}
