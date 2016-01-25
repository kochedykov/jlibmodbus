package com.sbpinvertor.modbus.data.base;

import com.sbpinvertor.modbus.ModbusFunction;
import com.sbpinvertor.modbus.data.ModbusInputStream;
import com.sbpinvertor.modbus.data.ModbusOutputStream;
import com.sbpinvertor.modbus.data.request.*;
import com.sbpinvertor.modbus.exception.ModbusNumberException;

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

    public ModbusRequest(ModbusMessage msg) {
        super(msg);
    }

    static public ModbusRequest createRequest(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        ModbusRequest request = null;
        int serverAddress = fifo.read();
        int functionCode = fifo.read();
        switch (ModbusFunction.getFunction(functionCode)) {
            case READ_COILS:
                request = new ReadCoilsRequest(serverAddress);
                break;
            case READ_DISCRETE_INPUTS:
                request = new ReadDiscreteInputsRequest(serverAddress);
                break;
            case READ_HOLDING_REGISTERS:
                request = new ReadHoldingRegistersRequest(serverAddress);
                break;
            case READ_INPUT_REGISTERS:
                request = new ReadInputRegistersRequest(serverAddress);
                break;
            case WRITE_SINGLE_COIL:
                request = new WriteSingleCoilRequest(serverAddress);
                break;
            case WRITE_SINGLE_REGISTER:
                request = new WriteSingleRegisterRequest(serverAddress);
                break;
            case WRITE_MULTIPLE_COILS:
                request = new WriteMultipleCoilsRequest(serverAddress);
                break;
            case WRITE_MULTIPLE_REGISTERS:
                request = new WriteMultipleRegistersRequest(serverAddress);
                break;
            case REPORT_SLAVE_ID:
            case READ_FILE_RECORD:
            case WRITE_FILE_RECORD:
                break;
        }
        request.read(fifo);
        return request;
    }

    abstract protected void writeRequest(ModbusOutputStream fifo) throws IOException;

    @Override
    final public void writePDU(ModbusOutputStream fifo) throws IOException {
        fifo.write(getFunction().getCode());
        writeRequest(fifo);
    }

    @Override
    final protected int pduSize() {
        return 1 + requestSize();
    }

    abstract protected int requestSize();
}
