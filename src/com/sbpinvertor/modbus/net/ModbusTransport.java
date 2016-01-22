package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.data.ModbusInputStream;
import com.sbpinvertor.modbus.data.ModbusOutputStream;
import com.sbpinvertor.modbus.data.ModbusResponseFactory;
import com.sbpinvertor.modbus.data.base.ModbusMessage;
import com.sbpinvertor.modbus.data.base.ModbusRequest;
import com.sbpinvertor.modbus.data.base.ModbusResponse;
import com.sbpinvertor.modbus.exception.ModbusTransportException;

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

    public ModbusResponse sendRequest(ModbusMessage msg) throws ModbusTransportException {
        try {
            send(msg);
            return ModbusResponseFactory.getResponse(getInputStream());
        } catch (Exception e) {
            throw new ModbusTransportException(e);
        }
    }

    public ModbusRequest recvRequest() throws ModbusTransportException {
        return null;
    }

    protected void send(ModbusMessage msg) throws ModbusTransportException {
        try {
            ModbusOutputStream os = getOutputStream();
            msg.write(os);
            os.flush();
        } catch (Exception e) {
            throw new ModbusTransportException(e);
        }
    }
}
