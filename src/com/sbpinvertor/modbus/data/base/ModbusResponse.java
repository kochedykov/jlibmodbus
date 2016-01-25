package com.sbpinvertor.modbus.data.base;

import com.sbpinvertor.modbus.ModbusFunction;
import com.sbpinvertor.modbus.data.ModbusInputStream;
import com.sbpinvertor.modbus.data.ModbusOutputStream;
import com.sbpinvertor.modbus.data.response.*;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusProtocolException;

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

public abstract class ModbusResponse extends ModbusMessage {

    private volatile ModbusException modbusException = ModbusException.NO_EXCEPTION;

    public ModbusResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public ModbusResponse(ModbusMessage msg) {
        super(msg);
    }

    static public ModbusResponse createResponse(ModbusInputStream fifo) throws ModbusNumberException, ModbusProtocolException, IOException {
        ModbusResponse response = null;
        int serverAddress = fifo.read();
        int functionCode = fifo.read();
        switch (ModbusFunction.getFunction(functionCode)) {
            case READ_COILS:
                response = new ReadCoilsResponse(serverAddress);
                break;
            case READ_DISCRETE_INPUTS:
                response = new ReadDiscreteInputsResponse(serverAddress);
                break;
            case READ_HOLDING_REGISTERS:
                response = new ReadHoldingRegistersResponse(serverAddress);
                break;
            case READ_INPUT_REGISTERS:
                response = new ReadInputRegistersResponse(serverAddress);
                break;
            case WRITE_SINGLE_COIL:
                response = new WriteSingleCoilResponse(serverAddress);
                break;
            case WRITE_SINGLE_REGISTER:
                response = new WriteSingleRegisterResponse(serverAddress);
                break;
            case WRITE_MULTIPLE_COILS:
                response = new WriteMultipleCoilsResponse(serverAddress);
                break;
            case WRITE_MULTIPLE_REGISTERS:
                response = new WriteMultipleRegistersResponse(serverAddress);
                break;
            case REPORT_SLAVE_ID:
            case READ_FILE_RECORD:
            case WRITE_FILE_RECORD:
                break;
        }
        if (response != null) {
            if (ModbusFunction.isException(functionCode)) {
                response.setException();
            }
            response.read(fifo);
            if (response.isException()) {
                throw new ModbusProtocolException(response.getException(), response.getServerAddress());
            }
        }
        return response;
    }

    final public boolean isException() {
        return modbusException != ModbusException.NO_EXCEPTION;
    }

    final public void setException() {
        modbusException = ModbusException.UNKNOWN_EXCEPTION;
    }

    @Override
    final protected void writePDU(ModbusOutputStream fifo) throws IOException {
        if (isException()) {
            fifo.write(getFunction().getExceptionCode());
            fifo.write(getModbusException());
        } else {
            fifo.write(getFunction().getCode());
            writeResponse(fifo);
        }
    }

    @Override
    final protected void readPDU(ModbusInputStream fifo) throws IOException {
        if (isException()) {
            setModbusException(fifo.read());
        } else {
            readResponse(fifo);
        }
    }

    abstract protected void readResponse(ModbusInputStream fifo) throws IOException;

    abstract public void writeResponse(ModbusOutputStream fifo) throws IOException;

    final public ModbusException getException() {
        return modbusException;
    }

    final public int getModbusException() {
        return getException().getCode();
    }

    final protected void setModbusException(int code) {
        modbusException = ModbusException.getExceptionCode(code);
    }

    @Override
    final protected int pduSize() {
        return 1 + responseSize();
    }

    abstract protected int responseSize();
}
