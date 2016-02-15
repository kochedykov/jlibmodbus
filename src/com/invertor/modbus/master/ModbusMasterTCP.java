package com.invertor.modbus.master;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.ModbusMaster;
import com.invertor.modbus.data.CommStatus;
import com.invertor.modbus.exception.IllegalFunctionException;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.net.ModbusConnection;
import com.invertor.modbus.net.ModbusMasterConnectionTCP;
import com.invertor.modbus.tcp.TcpParameters;
import com.invertor.modbus.utils.ModbusFunctionCode;

import java.util.concurrent.atomic.AtomicBoolean;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

final public class ModbusMasterTCP extends ModbusMaster {
    final private boolean keepAlive;
    final private ModbusConnection conn;
    final private AtomicBoolean connected = new AtomicBoolean(false);

    public ModbusMasterTCP(TcpParameters parameters) {
        conn = new ModbusMasterConnectionTCP(parameters);
        keepAlive = parameters.isKeepAlive();
        try {
            if (keepAlive) {
                open();
            }
        } catch (ModbusIOException e) {
            Modbus.log().warning("keepAlive is set, connection failed at creation time.");
        }
    }

    @Override
    protected void sendRequest(ModbusMessage msg) throws ModbusIOException {
        if (!keepAlive)
            open();
        try {
            super.sendRequest(msg);
        } catch (ModbusIOException e) {
            if (keepAlive) {
                open();
                super.sendRequest(msg);
            } else {
                throw e;
            }
        }
    }

    @Override
    protected ModbusMessage readResponse() throws ModbusNumberException, ModbusIOException, ModbusProtocolException {
        ModbusMessage msg = super.readResponse();
        if (!keepAlive) {
            close();
        }
        return msg;
    }

    @Override
    public void open() throws ModbusIOException {
        close();
        conn.open();
    }

    @Override
    public void close() throws ModbusIOException {
        conn.close();
    }

    @Override
    protected ModbusConnection getConnection() {
        return conn;
    }

    @Override
    public int readExceptionStatus(int serverAddress) throws ModbusNumberException, ModbusIOException, ModbusProtocolException {
        throw new IllegalFunctionException(ModbusFunctionCode.READ_EXCEPTION_STATUS.toInt());
    }

    @Override
    public byte[] reportSlaveId(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        throw new IllegalFunctionException(ModbusFunctionCode.REPORT_SLAVE_ID.toInt());
    }

    @Override
    public CommStatus getCommEventCounter(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        throw new IllegalFunctionException(ModbusFunctionCode.GET_COMM_EVENT_COUNTER.toInt());
    }
}
