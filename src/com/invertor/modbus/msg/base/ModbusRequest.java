package com.invertor.modbus.msg.base;

import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
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
