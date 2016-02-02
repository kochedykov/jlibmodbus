package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.net.stream.InputStreamASCII;
import com.sbpinvertor.modbus.net.stream.OutputStreamASCII;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.net.stream.base.ModbusOutputStream;
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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ModbusConnectionASCII extends ModbusConnectionSerial {
    final private OutputStreamASCII os;
    final private InputStreamASCII is;

    public ModbusConnectionASCII(SerialPort serial) {
        super(serial);
        os = new OutputStreamASCII(serial);
        is = new InputStreamASCII(serial);
    }

    @Override
    public void reset() {
        super.reset();
        is.reset();
        os.reset();
    }

    @Override
    public ModbusOutputStream getOutputStream() {
        return os;
    }

    @Override
    public ModbusInputStream getInputStream() {
        return is;
    }
}
