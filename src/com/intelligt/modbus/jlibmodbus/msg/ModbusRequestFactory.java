package com.intelligt.modbus.jlibmodbus.msg;

import com.intelligt.modbus.jlibmodbus.msg.base.ModbusMessage;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.request.*;
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
final public class ModbusRequestFactory implements ModbusMessageFactory {

    private ModbusRequestFactory() {

    }

    static public ModbusRequestFactory getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * This method creates a #ModbusRequest instance from #functionCode
     *
     * @param functionCode a number representing a modbus function
     * @return an instance of a specific ModbusRequest
     * @see ModbusRequest
     * @see ModbusMessageFactory
     * @see ModbusResponseFactory
     */
    @Override
    public ModbusMessage createMessage(int functionCode) {
        ModbusMessage msg;

        switch (ModbusFunctionCode.get(functionCode)) {
            case READ_COILS:
                msg = new ReadCoilsRequest();
                break;
            case READ_DISCRETE_INPUTS:
                msg = new ReadDiscreteInputsRequest();
                break;
            case READ_HOLDING_REGISTERS:
                msg = new ReadHoldingRegistersRequest();
                break;
            case READ_INPUT_REGISTERS:
                msg = new ReadInputRegistersRequest();
                break;
            case WRITE_SINGLE_COIL:
                msg = new WriteSingleCoilRequest();
                break;
            case WRITE_SINGLE_REGISTER:
                msg = new WriteSingleRegisterRequest();
                break;
            case WRITE_MULTIPLE_COILS:
                msg = new WriteMultipleCoilsRequest();
                break;
            case WRITE_MULTIPLE_REGISTERS:
                msg = new WriteMultipleRegistersRequest();
                break;
            case MASK_WRITE_REGISTER:
                msg = new MaskWriteRegisterRequest();
                break;
            case READ_WRITE_MULTIPLE_REGISTERS:
                msg = new ReadWriteMultipleRegistersRequest();
                break;
            case READ_FIFO_QUEUE:
                msg = new ReadFifoQueueRequest();
                break;
            case READ_FILE_RECORD:
                msg = new ReadFileRecordRequest();
                break;
            case WRITE_FILE_RECORD:
                msg = new WriteFileRecordRequest();
                break;
            case READ_EXCEPTION_STATUS:
                msg = new ReadExceptionStatusRequest();
                break;
            case REPORT_SLAVE_ID:
                msg = new ReportSlaveIdRequest();
                break;
            case GET_COMM_EVENT_COUNTER:
                msg = new GetCommEventCounterRequest();
                break;
            case GET_COMM_EVENT_LOG:
                msg = new GetCommEventLogRequest();
                break;
            case DIAGNOSTICS:
                msg = new DiagnosticsRequest();
                break;
            case ENCAPSULATED_INTERFACE_TRANSPORT:
                msg = new EncapsulatedInterfaceTransportRequest();
                break;
            default:
                msg = new IllegalFunctionRequest(functionCode);
        }
        return msg;
    }

    static private class SingletonHolder {
        final static private ModbusRequestFactory instance = new ModbusRequestFactory();
    }
}
