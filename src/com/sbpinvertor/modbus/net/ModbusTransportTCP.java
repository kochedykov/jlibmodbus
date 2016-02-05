package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.exception.ModbusIOException;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.msg.ModbusMessageFactory;
import com.sbpinvertor.modbus.msg.base.ModbusMessage;
import com.sbpinvertor.modbus.net.stream.InputStreamTCP;
import com.sbpinvertor.modbus.net.stream.OutputStreamTCP;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.net.stream.base.ModbusOutputStream;
import com.sbpinvertor.modbus.tcp.TcpAduHeader;

import java.net.Socket;

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
public class ModbusTransportTCP extends ModbusTransport {

    public ModbusTransportTCP(Socket socket) throws ModbusIOException {
        super(new InputStreamTCP(socket), new OutputStreamTCP(socket));
    }

    @Override
    protected ModbusMessage read(ModbusMessageFactory factory) throws ModbusNumberException, ModbusIOException {
        ModbusInputStream is = getInputStream();
        TcpAduHeader header = new TcpAduHeader();
        header.read(is);
        ModbusMessage msg = factory.createMessage(is);
        msg.setTransactionId(header.getTransactionId());
        msg.setProtocolId(header.getProtocolId());
        return msg;
    }

    @Override
    public void sendImpl(ModbusMessage msg) throws ModbusIOException {
        ModbusOutputStream os = getOutputStream();
        TcpAduHeader header = new TcpAduHeader();
        header.setProtocolId(msg.getProtocolId());
        header.setTransactionId(msg.getTransactionId());
        header.setPduSize(msg.size());
        header.write(os);
        msg.write(os);
    }
}
