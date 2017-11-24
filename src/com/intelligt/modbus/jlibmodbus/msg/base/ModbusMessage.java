package com.intelligt.modbus.jlibmodbus.msg.base;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;

import java.io.IOException;

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

abstract public class ModbusMessage {

    private int serverAddress = Modbus.TCP_DEFAULT_ID;
    private int protocolId = Modbus.PROTOCOL_ID;
    private int transactionId = 0;

    public ModbusMessage() {

    }

    final public void write(ModbusOutputStream fifo) throws ModbusIOException {
        try {
            fifo.write(getServerAddress());
            writePDU(fifo);
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
    }

    final public void read(ModbusInputStream fifo) throws ModbusNumberException, ModbusIOException {
        try {
            readPDU(fifo);
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
    }

    abstract public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException;

    abstract protected void writePDU(ModbusOutputStream fifo) throws IOException;

    abstract public int getFunction();

    public int size() {
        return 1 + pduSize();
    }

    abstract protected int pduSize();

    public int getServerAddress() {
        return serverAddress;
    }

    /*
        protected ModbusMessage(int serverAddress) throws ModbusNumberException {
            setServerAddress(serverAddress);
        }
    */
    public void setServerAddress(int serverAddress) throws ModbusNumberException {
        if (!Modbus.checkServerAddress(serverAddress))
            throw new ModbusNumberException("Error in slave id", serverAddress);
        this.serverAddress = serverAddress;
    }

    public int getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(int protocolId) {
        this.protocolId = protocolId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
