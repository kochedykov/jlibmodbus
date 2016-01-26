package com.sbpinvertor.modbus.data;

import com.sbpinvertor.modbus.ModbusFunction;
import com.sbpinvertor.modbus.data.base.ModbusMessage;
import com.sbpinvertor.modbus.data.base.ModbusRequest;
import com.sbpinvertor.modbus.data.request.*;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
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
final public class ModbusRequestFactory implements ModbusMessageFactory {

    private ModbusRequestFactory() {

    }

    static public ModbusRequestFactory getInstance() {
        return SingletonHolder.instance;
    }

    public ModbusRequest createReadCoils(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadCoilsRequest(serverAddress, startAddress, quantity);
    }

    public ModbusRequest createReadDiscreteInputs(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadDiscreteInputsRequest(serverAddress, startAddress, quantity);
    }

    public ModbusRequest createReadInputRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadInputRegistersRequest(serverAddress, startAddress, quantity);
    }

    public ModbusRequest createReadHoldingRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadHoldingRegistersRequest(serverAddress, startAddress, quantity);
    }

    public ModbusRequest createWriteSingleCoil(int serverAddress, int startAddress, boolean coil) throws ModbusNumberException {
        return new WriteSingleCoilRequest(serverAddress, startAddress, coil);
    }

    public ModbusRequest createWriteMultipleCoils(int serverAddress, int startAddress, boolean[] coils) throws ModbusNumberException {
        return new WriteMultipleCoilsRequest(serverAddress, startAddress, coils);
    }

    public ModbusRequest createWriteMultipleRegisters(int serverAddress, int startAddress, int[] registers) throws ModbusNumberException {
        return new WriteMultipleRegistersRequest(serverAddress, startAddress, registers);
    }

    public ModbusRequest createWriteSingleRegister(int serverAddress, int startAddress, int register) throws ModbusNumberException {
        return new WriteSingleRegisterRequest(serverAddress, startAddress, register);
    }

    @Override
    public ModbusMessage createMessage(ModbusInputStream fifo) throws ModbusTransportException, ModbusNumberException {
        try {
            ModbusMessage msg;
            int serverAddress = fifo.read();
            int functionCode = fifo.read();
            switch (ModbusFunction.getFunction(functionCode)) {
                case READ_COILS:
                    msg = new ReadCoilsRequest(serverAddress);
                    break;
                case READ_DISCRETE_INPUTS:
                    msg = new ReadDiscreteInputsRequest(serverAddress);
                    break;
                case READ_HOLDING_REGISTERS:
                    msg = new ReadHoldingRegistersRequest(serverAddress);
                    break;
                case READ_INPUT_REGISTERS:
                    msg = new ReadInputRegistersRequest(serverAddress);
                    break;
                case WRITE_SINGLE_COIL:
                    msg = new WriteSingleCoilRequest(serverAddress);
                    break;
                case WRITE_SINGLE_REGISTER:
                    msg = new WriteSingleRegisterRequest(serverAddress);
                    break;
                case WRITE_MULTIPLE_COILS:
                    msg = new WriteMultipleCoilsRequest(serverAddress);
                    break;
                case WRITE_MULTIPLE_REGISTERS:
                    msg = new WriteMultipleRegistersRequest(serverAddress);
                    break;
                case REPORT_SLAVE_ID:
                case READ_FILE_RECORD:
                case WRITE_FILE_RECORD:
                default:
                    throw new ModbusTransportException("function " + functionCode + " not supported.");
            }
            msg.read(fifo);
            return msg;
        } catch (IOException e) {
            throw new ModbusTransportException(e);
        }
    }

    static private class SingletonHolder {
        final static private ModbusRequestFactory instance = new ModbusRequestFactory();
    }
}
