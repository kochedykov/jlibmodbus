package com.intelligt.modbus.jlibmodbus.msg;

import com.intelligt.modbus.jlibmodbus.msg.base.ModbusMessage;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.*;
import com.intelligt.modbus.jlibmodbus.utils.ModbusFunctionCode;

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

    /**
     * This method creates a #ModbusResponse instance from #functionCode
     *
     * @param functionCode a number representing a modbus function
     * @return an instance of a specific ModbusRequest
     * @see ModbusRequest
     * @see ModbusMessageFactory
     * @see ModbusResponseFactory
     */
    @Override
    public ModbusMessage createMessage(int functionCode) {
        ModbusResponse msg;

        switch (ModbusFunctionCode.get(functionCode)) {
            case READ_COILS:
                msg = new ReadCoilsResponse();
                break;
            case READ_DISCRETE_INPUTS:
                msg = new ReadDiscreteInputsResponse();
                break;
            case READ_HOLDING_REGISTERS:
                msg = new ReadHoldingRegistersResponse();
                break;
            case READ_INPUT_REGISTERS:
                msg = new ReadInputRegistersResponse();
                break;
            case WRITE_SINGLE_COIL:
                msg = new WriteSingleCoilResponse();
                break;
            case WRITE_SINGLE_REGISTER:
                msg = new WriteSingleRegisterResponse();
                break;
            case WRITE_MULTIPLE_COILS:
                msg = new WriteMultipleCoilsResponse();
                break;
            case WRITE_MULTIPLE_REGISTERS:
                msg = new WriteMultipleRegistersResponse();
                break;
            case MASK_WRITE_REGISTER:
                msg = new MaskWriteRegisterResponse();
                break;
            case READ_WRITE_MULTIPLE_REGISTERS:
                msg = new ReadWriteMultipleRegistersResponse();
                break;
            case READ_FIFO_QUEUE:
                msg = new ReadFifoQueueResponse();
                break;
            case READ_FILE_RECORD:
                msg = new ReadFileRecordResponse();
                break;
            case WRITE_FILE_RECORD:
                msg = new WriteFileRecordResponse();
                break;
            case READ_EXCEPTION_STATUS:
                msg = new ReadExceptionStatusResponse();
                break;
            case REPORT_SLAVE_ID:
                msg = new ReportSlaveIdResponse();
                break;
            case GET_COMM_EVENT_COUNTER:
                msg = new GetCommEventCounterResponse();
                break;
            case GET_COMM_EVENT_LOG:
                msg = new GetCommEventLogResponse();
                break;
            case DIAGNOSTICS:
                msg = new DiagnosticsResponse();
                break;
            case ENCAPSULATED_INTERFACE_TRANSPORT:
                msg = new EncapsulatedInterfaceTransportResponse();
                break;
            default:
                msg = new IllegalFunctionResponse(functionCode);
        }
        if (ModbusFunctionCode.isException(functionCode)) {
            msg.setException();
        }
        return msg;
    }

    static private class SingletonHolder {
        final static private ModbusResponseFactory instance = new ModbusResponseFactory();
    }
}
