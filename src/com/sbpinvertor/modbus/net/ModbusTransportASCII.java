package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusProtocolException;
import com.sbpinvertor.modbus.msg.ModbusMessageFactory;
import com.sbpinvertor.modbus.msg.base.ModbusMessage;
import com.sbpinvertor.modbus.net.stream.InputStreamASCII;
import com.sbpinvertor.modbus.net.stream.OutputStreamASCII;
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
public class ModbusTransportASCII extends ModbusTransport {

    public ModbusTransportASCII(SerialPort serial) {
        super(new InputStreamASCII(serial), new OutputStreamASCII(serial));
    }

    @Override
    protected ModbusMessage read(ModbusMessageFactory factory) throws ModbusNumberException, IOException, ModbusProtocolException {
        InputStreamASCII is = (InputStreamASCII) getInputStream();
        ModbusMessage msg = factory.createMessage(is);
        int lrc = is.getLrc();
        boolean check = lrc != is.read();
        if (is.readByte() != Modbus.ASCII_CODE_CR || is.readByte() != Modbus.ASCII_CODE_LF)
            Modbus.log().warning("\\r\\n not received.");
        /*clear fifo*/
        is.reset();
        if (!check) {
            throw new ModbusNumberException("control sum check failed.", lrc);
        }
        return msg;
    }

    @Override
    protected void sendImpl(ModbusMessage msg) throws IOException {
        msg.write(getOutputStream());
    }
}
