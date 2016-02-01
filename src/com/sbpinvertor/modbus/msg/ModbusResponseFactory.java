package com.sbpinvertor.modbus.msg;

import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.msg.base.ModbusMessage;
import com.sbpinvertor.modbus.msg.base.ModbusResponse;
import com.sbpinvertor.modbus.msg.response.*;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.utils.ModbusFunctionCode;

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
final public class ModbusResponseFactory implements ModbusMessageFactory {

    private ModbusResponseFactory() {

    }

    static public ModbusResponseFactory getInstance() {
        return SingletonHolder.instance;
    }

    public ModbusResponse createReadCoils(int serverAddress, boolean[] coils) throws ModbusNumberException {
        return new ReadCoilsResponse(serverAddress, coils);
    }

    public ModbusResponse createReadDiscreteInputs(int serverAddress, boolean[] discreteInputs) throws ModbusNumberException {
        return new ReadDiscreteInputsResponse(serverAddress, discreteInputs);
    }

    public ModbusResponse createReadInputRegisters(int serverAddress, int[] inputRegisters) throws ModbusNumberException {
        return new ReadInputRegistersResponse(serverAddress, inputRegisters);
    }

    public ModbusResponse createReadHoldingRegisters(int serverAddress, int[] holdingRegisters) throws ModbusNumberException {
        return new ReadHoldingRegistersResponse(serverAddress, holdingRegisters);
    }

    public ModbusResponse createWriteSingleCoil(int serverAddress, int startAddress, boolean coil) throws ModbusNumberException {
        return new WriteSingleCoilResponse(serverAddress, startAddress, coil);
    }

    public ModbusResponse createWriteMultipleCoils(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new WriteMultipleCoilsResponse(serverAddress, startAddress, quantity);
    }

    public ModbusResponse createWriteMultipleRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new WriteMultipleRegistersResponse(serverAddress, startAddress, quantity);
    }

    public ModbusResponse createWriteSingleRegister(int serverAddress, int startAddress, int register) throws ModbusNumberException {
        return new WriteSingleRegisterResponse(serverAddress, startAddress, register);
    }

    @Override
    public ModbusMessage createMessage(ModbusInputStream fifo) throws ModbusTransportException, ModbusNumberException, IOException {
        ModbusResponse msg;
        int serverAddress = fifo.read();
        int functionCode = fifo.read();
        switch (ModbusFunctionCode.getFunction(functionCode)) {
            case READ_COILS:
                msg = new ReadCoilsResponse(serverAddress);
                break;
            case READ_DISCRETE_INPUTS:
                msg = new ReadDiscreteInputsResponse(serverAddress);
                break;
            case READ_HOLDING_REGISTERS:
                msg = new ReadHoldingRegistersResponse(serverAddress);
                break;
            case READ_INPUT_REGISTERS:
                msg = new ReadInputRegistersResponse(serverAddress);
                break;
            case WRITE_SINGLE_COIL:
                msg = new WriteSingleCoilResponse(serverAddress);
                break;
            case WRITE_SINGLE_REGISTER:
                msg = new WriteSingleRegisterResponse(serverAddress);
                break;
            case WRITE_MULTIPLE_COILS:
                msg = new WriteMultipleCoilsResponse(serverAddress);
                break;
            case WRITE_MULTIPLE_REGISTERS:
                msg = new WriteMultipleRegistersResponse(serverAddress);
                break;
            case REPORT_SLAVE_ID:
            case READ_FILE_RECORD:
            case WRITE_FILE_RECORD:
            default:
                throw new ModbusTransportException("function " + functionCode + " not supported.");
        }
        if (ModbusFunctionCode.isException(functionCode)) {
            msg.setException();
        }
        msg.read(fifo);
        return msg;
    }

    static private class SingletonHolder {
        final static private ModbusResponseFactory instance = new ModbusResponseFactory();
    }
}
