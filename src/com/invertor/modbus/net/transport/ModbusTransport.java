package com.invertor.modbus.net.transport;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.ModbusMessageFactory;
import com.invertor.modbus.msg.ModbusRequestFactory;
import com.invertor.modbus.msg.ModbusResponseFactory;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.net.stream.base.LoggingInputStream;
import com.invertor.modbus.net.stream.base.LoggingOutputStream;

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
public abstract class ModbusTransport {

    final private LoggingInputStream is;
    final private LoggingOutputStream os;

    ModbusTransport(LoggingInputStream is, LoggingOutputStream os) {
        this.is = is;
        this.os = os;
    }

    public void close() throws IOException {
        is.close();
        os.close();
    }

    public ModbusMessage readRequest() throws ModbusNumberException, ModbusIOException {
        return readMessage(ModbusRequestFactory.getInstance());
    }

    public ModbusMessage readResponse() throws ModbusNumberException, ModbusIOException {
        return readMessage(ModbusResponseFactory.getInstance());
    }

    final public ModbusMessage readMessage(ModbusMessageFactory factory) throws ModbusNumberException, ModbusIOException {
        try {
            return read(factory);
        } finally {
            getInputStream().log();
        }
    }

    public void send(ModbusMessage msg) throws ModbusIOException {
        try {
            sendImpl(msg);
        } finally {
            getOutputStream().log();
        }
        try {
            getOutputStream().flush();
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }

    }

    public LoggingInputStream getInputStream() {
        return is;
    }

    public LoggingOutputStream getOutputStream() {
        return os;
    }

    abstract protected ModbusMessage read(ModbusMessageFactory factory) throws ModbusNumberException, ModbusIOException;

    abstract protected void sendImpl(ModbusMessage msg) throws ModbusIOException;
}
