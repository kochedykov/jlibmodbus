package com.invertor.modbus.msg;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.*;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.utils.ModbusFunctionCode;

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
final public class ModbusResponseFactory implements ModbusMessageFactory {

    private ModbusResponseFactory() {

    }

    static public ModbusResponseFactory getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public ModbusMessage createMessage(ModbusInputStream fifo) throws ModbusNumberException, ModbusIOException {
        ModbusResponse msg;
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
            case MASK_WRITE_REGISTER:
                msg = new MaskWriteRegisterResponse(serverAddress);
                break;
            case READ_WRITE_MULTIPLE_REGISTERS:
                msg = new ReadWriteMultipleRegistersResponse(serverAddress);
                break;
            case READ_EXCEPTION_STATUS:
                msg = new ReadExceptionStatusResponse(serverAddress);
                break;
            case REPORT_SLAVE_ID:
                msg = new ReportSlaveIdResponse(serverAddress);
                break;
            case GET_COMM_EVENT_COUNTER:
                msg = new GetCommEventCounterResponse(serverAddress);
                break;
            case GET_COMM_EVENT_LOG:
            case READ_FILE_RECORD:
            case WRITE_FILE_RECORD:
            case DIAGNOSTICS:
            case READ_FIFO_QUEUE:
            case ENCAPSULATED_INTERFACE_TRANSPORT:
            case CAN_OPEN_PDU:
            case READ_DEVICE_IDENTIFICATION:
            default:
                throw new ModbusIOException("Illegal function code");
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
