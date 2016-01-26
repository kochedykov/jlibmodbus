package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.msg.ModbusMessageFactory;
import com.sbpinvertor.modbus.msg.base.ModbusMessage;
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
abstract public class ModbusTransport {

    private volatile int responseTimeout = Modbus.MAX_RESPONSE_TIMEOUT;

    public int getResponseTimeout() {
        return responseTimeout;
    }

    public void setResponseTimeout(int responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    abstract public ModbusOutputStream getOutputStream();
    abstract public ModbusInputStream getInputStream();

    public ModbusMessage recv(ModbusMessageFactory factory) throws ModbusTransportException {
        try {
            return factory.createMessage(getInputStream());
        } catch (Exception e) {
            throw new ModbusTransportException(e);
        }
    }

    public void send(ModbusMessage msg) throws ModbusTransportException {
        try {
            ModbusOutputStream os = getOutputStream();
            msg.write(os);
            os.flush();
        } catch (IOException e) {
            throw new ModbusTransportException(e);
        }
    }
}
