package com.intelligt.modbus.jlibmodbus.net.transport;

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.ModbusMessageFactory;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusMessage;
import com.intelligt.modbus.jlibmodbus.net.stream.InputStreamTCP;
import com.intelligt.modbus.jlibmodbus.net.stream.OutputStreamTCP;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.tcp.TcpAduHeader;

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

    final private Socket socket;

    ModbusTransportTCP(Socket socket) throws IOException {
        super(new InputStreamTCP(socket), new OutputStreamTCP(socket));
        this.socket = socket;
    }

    @Override
    protected ModbusMessage read(ModbusMessageFactory factory) throws ModbusNumberException, ModbusIOException {
        ModbusInputStream is = getInputStream();
        TcpAduHeader header = new TcpAduHeader();
        header.read(is);
        try {
            ModbusMessage msg = createMessage(factory);

            msg.setTransactionId(header.getTransactionId());
            msg.setProtocolId(header.getProtocolId());

            return msg;
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
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
        socket.close();
    }
}
