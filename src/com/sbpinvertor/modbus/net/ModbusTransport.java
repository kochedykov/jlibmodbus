package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.msg.ModbusMessageFactory;
import com.sbpinvertor.modbus.msg.ModbusRequestFactory;
import com.sbpinvertor.modbus.msg.ModbusResponseFactory;
import com.sbpinvertor.modbus.msg.base.ModbusMessage;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.net.stream.base.ModbusOutputStream;

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
abstract public class ModbusTransport {

    final private ModbusInputStream is;
    final private ModbusOutputStream os;

    public ModbusTransport(ModbusInputStream is, ModbusOutputStream os) {
        this.is = is;
        this.os = os;
    }

    public ModbusMessage readRequest() throws ModbusNumberException, ModbusTransportException, IOException {
        return read(ModbusRequestFactory.getInstance());
    }

    public ModbusMessage readResponse() throws ModbusNumberException, ModbusTransportException, IOException {
        return read(ModbusResponseFactory.getInstance());
    }

    public void send(ModbusMessage msg) throws ModbusTransportException, IOException {
        sendImpl(msg);
        os.flush();
    }

    public ModbusInputStream getInputStream() {
        return is;
    }

    public ModbusOutputStream getOutputStream() {
        return os;
    }

    abstract protected ModbusMessage read(ModbusMessageFactory factory) throws ModbusNumberException, ModbusTransportException, IOException;

    abstract protected void sendImpl(ModbusMessage msg) throws IOException;
}
