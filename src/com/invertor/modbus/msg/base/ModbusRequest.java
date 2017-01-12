package com.invertor.modbus.msg.base;

import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;

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
abstract public class ModbusRequest extends ModbusMessage {

    public ModbusRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    abstract public void writeRequest(ModbusOutputStream fifo) throws IOException;

    @Override
    final public void writePDU(ModbusOutputStream fifo) throws IOException {
        fifo.write(getFunction());
        writeRequest(fifo);
    }

    @Override
    final protected int pduSize() {
        return 1 + requestSize();
    }

    abstract public int requestSize();

    abstract public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException;

    abstract protected boolean validateResponseImpl(ModbusResponse response);

    public void validateResponse(ModbusResponse msg) throws ModbusNumberException {
        if (getProtocolId() != msg.getProtocolId())
            throw new ModbusNumberException("Collision: does not matches the transaction id");
        if (getTransactionId() != msg.getTransactionId())
            throw new ModbusNumberException("Collision: does not matches the transaction id");
        if (getServerAddress() != msg.getServerAddress())
            throw new ModbusNumberException("Does not matches the slave address", msg.getServerAddress());
        if (getFunction() != msg.getFunction())
            throw new ModbusNumberException("Does not matches the function code", msg.getFunction());
        if (!msg.isException()) {
            if (!validateResponseImpl(msg))
                throw new ModbusNumberException("Collision: response does not matches the request");
        }
    }
}
