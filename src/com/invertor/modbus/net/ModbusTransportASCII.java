package com.invertor.modbus.net;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.ModbusMessageFactory;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.net.stream.InputStreamASCII;
import com.invertor.modbus.net.stream.OutputStreamASCII;
import com.invertor.modbus.serial.SerialPort;

import java.io.IOException;

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
public class ModbusTransportASCII extends ModbusTransport {

    public ModbusTransportASCII(SerialPort serial) {
        super(new InputStreamASCII(serial), new OutputStreamASCII(serial));
    }

    @Override
    protected ModbusMessage read(ModbusMessageFactory factory) throws ModbusNumberException, ModbusIOException {
        int cr;
        int lf;
        boolean check;
        int lrc;
        ModbusMessage msg;
        InputStreamASCII is = (InputStreamASCII) getInputStream();
        try {
            msg = factory.createMessage(is);
            lrc = is.getLrc();
            check = lrc == is.read();
            cr = is.readRaw();
            lf = is.readRaw();
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
        if (cr != Modbus.ASCII_CODE_CR || lf != Modbus.ASCII_CODE_LF)
            Modbus.log().warning("\\r\\n not received.");
        /*clear fifo*/

        if (!check) {
            throw new ModbusNumberException("control sum check failed.", lrc);
        }
        is.reset();
        return msg;
    }

    @Override
    protected void sendImpl(ModbusMessage msg) throws ModbusIOException {
        msg.write(getOutputStream());
    }
}
