package com.sbpinvertor.modbus.data;

import com.sbpinvertor.modbus.ModbusFunction;
import com.sbpinvertor.modbus.data.base.ModbusMessage;
import com.sbpinvertor.modbus.data.base.ModbusResponse;
import com.sbpinvertor.modbus.data.response.*;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusProtocolException;
import com.sbpinvertor.modbus.exception.ModbusTransportException;

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

    static public ModbusResponse createReadCoils(int serverAddress, boolean[] coils) throws ModbusNumberException {
        return new ReadCoilsResponse(serverAddress, coils);
    }

    static public ModbusResponse createReadDiscreteInputs(int serverAddress, boolean[] discreteInputs) throws ModbusNumberException {
        return new ReadDiscreteInputsResponse(serverAddress, discreteInputs);
    }

    static public ModbusResponse createReadInputRegisters(int serverAddress, int[] inputRegisters) throws ModbusNumberException {
        return new ReadInputRegistersResponse(serverAddress, inputRegisters);
    }

    static public ModbusResponse createReadHoldingRegisters(int serverAddress, int[] holdingRegisters) throws ModbusNumberException {
        return new ReadHoldingRegistersResponse(serverAddress, holdingRegisters);
    }

    static public ModbusResponse createWriteSingleCoil(int serverAddress, int startAddress, boolean coil) throws ModbusNumberException {
        return new WriteSingleCoilResponse(serverAddress, startAddress, coil);
    }

    static public ModbusResponse createWriteMultipleCoils(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new WriteMultipleCoilsResponse(serverAddress, startAddress, quantity);
    }

    static public ModbusResponse createWriteMultipleRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new WriteMultipleRegistersResponse(serverAddress, startAddress, quantity);
    }

    static public ModbusResponse createWriteSingleRegister(int serverAddress, int startAddress, int register) throws ModbusNumberException {
        return new WriteSingleRegisterResponse(serverAddress, startAddress, register);
    }

    @Override
    public ModbusMessage createMessage(ModbusInputStream fifo) throws ModbusTransportException, ModbusNumberException {
        try {
            ModbusResponse msg = null;
            int serverAddress = fifo.read();
            int functionCode = fifo.read();
            switch (ModbusFunction.getFunction(functionCode)) {
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
                    break;
            }
            if (msg != null) {
                if (ModbusFunction.isException(functionCode)) {
                    msg.setException();
                }
                msg.read(fifo);
                if (msg.isException()) {
                    throw new ModbusProtocolException(msg.getException(), msg.getServerAddress());
                }
            }
            return msg;
        } catch (IOException e) {
            throw new ModbusTransportException(e);
        }
    }

    static private class SingletonHolder {
        final static private ModbusResponseFactory instance = new ModbusResponseFactory();
    }
}
