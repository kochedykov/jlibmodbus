package com.sbpinvertor.modbus.msg;

import com.sbpinvertor.modbus.exception.ModbusIOException;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.msg.base.ModbusMessage;
import com.sbpinvertor.modbus.msg.base.ModbusRequest;
import com.sbpinvertor.modbus.msg.request.*;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.utils.ModbusFunctionCode;

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

    public ModbusRequest createMaskWriteRegister(int serverAddress, int startAddress, int and, int or) throws ModbusNumberException {
        return new MaskWriteRegisterRequest(serverAddress, startAddress, and, or);
    }

    public ModbusRequest createReadExceptionStatus(int serverAddress) throws ModbusNumberException {
        return new ReadExceptionStatusRequest(serverAddress);
    }

    public ModbusRequest createReportSlaveId(int serverAddress) throws ModbusNumberException {
        return new ReportSlaveIdRequest(serverAddress);
    }

    @Override
    public ModbusMessage createMessage(ModbusInputStream fifo) throws ModbusNumberException, ModbusIOException {
        ModbusMessage msg;
        int serverAddress;
        int functionCode;
        try {
            serverAddress = fifo.read();
            functionCode = fifo.read();
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
        switch (ModbusFunctionCode.getFunctionCode(functionCode)) {
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
            case READ_EXCEPTION_STATUS:
                msg = new ReadExceptionStatusRequest(serverAddress);
                break;
            case REPORT_SLAVE_ID:
                msg = new ReportSlaveIdRequest(serverAddress);
                break;
            case READ_FILE_RECORD:
            case WRITE_FILE_RECORD:
            case MASK_WRITE_REGISTER:
                msg = new MaskWriteRegisterRequest(serverAddress);
                break;
            case DIAGNOSTICS:
            case GET_COMM_EVENT_COUNTER:
            case GET_COMM_EVENT_LOG:
            case READ_WRITE_MULTIPLE_REGISTERS:
            case READ_FIFO_QUEUE:
            case ENCAPSULATED_INTERFACE_TRANSPORT:
            case CAN_OPEN_PDU:
            case READ_DEVICE_IDENTIFICATION:
            default:
                throw new ModbusIOException("Illegal function code");
        }
        msg.read(fifo);
        return msg;
    }

    static private class SingletonHolder {
        final static private ModbusRequestFactory instance = new ModbusRequestFactory();
    }
}
