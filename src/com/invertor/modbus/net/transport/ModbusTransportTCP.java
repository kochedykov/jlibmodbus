package com.invertor.modbus.net.transport;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.ModbusMessageFactory;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.net.stream.InputStreamTCP;
import com.invertor.modbus.net.stream.OutputStreamTCP;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.tcp.TcpAduHeader;

import java.io.IOException;
import java.net.Socket;

/*
 * Copyright (C) 2016 "Invertor" Factory", JSC
 * [http://www.sbp-invertor.ru]
 *
 * This file is part of JLibModbus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
class ModbusTransportTCP extends ModbusTransport {

    ModbusTransportTCP(Socket socket) throws ModbusIOException, IOException {
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

    @Override
    public void close() throws IOException {
        super.close();
    }
}
