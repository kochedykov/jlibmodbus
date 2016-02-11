package com.invertor.modbus.net;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.ModbusMessageFactory;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.net.stream.InputStreamRTU;
import com.invertor.modbus.net.stream.OutputStreamRTU;
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
public class ModbusTransportRTU extends ModbusTransport {

    public ModbusTransportRTU(SerialPort serial) {
        super(new InputStreamRTU(serial), new OutputStreamRTU(serial));
    }

    @Override
    protected ModbusMessage read(ModbusMessageFactory factory) throws ModbusIOException, ModbusNumberException {
        ModbusMessage msg;
        InputStreamRTU is = (InputStreamRTU) getInputStream();
        try {
            msg = factory.createMessage(is);
            int r_crc = is.readShortLE();
            // crc from the same crc equals zero
            int c_crc = is.getCrc();
            if (c_crc != 0 || r_crc == 0) {
                throw new ModbusNumberException("control sum check failed.", r_crc);
            }
        } catch (IOException ioe) {
            throw new ModbusIOException(ioe);
        } finally {
            is.reset();
        }
        return msg;
    }

    @Override
    protected void sendImpl(ModbusMessage msg) throws ModbusIOException {
        msg.write(getOutputStream());
    }
}
