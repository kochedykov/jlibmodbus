package com.invertor.modbus.msg;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.*;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.utils.ModbusFunctionCode;

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

        switch (ModbusFunctionCode.get(functionCode)) {
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
            case READ_FIFO_QUEUE:
                msg = new ReadFifoQueueResponse(serverAddress);
                break;
            case READ_FILE_RECORD:
                msg = new ReadFileRecordResponse(serverAddress);
                break;
            case WRITE_FILE_RECORD:
                msg = new WriteFileRecordResponse(serverAddress);
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
                msg = new GetCommEventLogResponse(serverAddress);
                break;
            case DIAGNOSTICS:
                msg = new DiagnosticsResponse(serverAddress);
                break;
            case ENCAPSULATED_INTERFACE_TRANSPORT:
                msg = new EncapsulatedInterfaceTransportResponse(serverAddress);
                break;
            default:
                throw new ModbusNumberException("Unknown function code", functionCode);
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
