package com.sbpinvertor.modbus.net.stream;

import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.serial.SerialPort;

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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
abstract public class InputStreamSerial extends ModbusInputStream {

    final private SerialPort serial;

    InputStreamSerial(SerialPort serial) {
        this.serial = serial;
    }

    @Override
    public int read() throws IOException {
        return serial.readByte(getReadTimeout()) & 0xff;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return serial.read(b, off, len);
    }

    @Override
    public void reset() {

    }
}
