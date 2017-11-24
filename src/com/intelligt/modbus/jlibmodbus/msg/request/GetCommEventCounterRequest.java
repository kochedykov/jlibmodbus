package com.intelligt.modbus.jlibmodbus.msg.request;

import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.GetCommEventCounterResponse;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.ModbusFunctionCode;

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
final public class GetCommEventCounterRequest extends ModbusRequest {

    public GetCommEventCounterRequest() {
        super();
    }

    @Override
    protected Class getResponseClass() {
        return GetCommEventCounterResponse.class;
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        //no op
    }

    @Override
    public int requestSize() {
        return 0;
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        GetCommEventCounterResponse response = new GetCommEventCounterResponse();
        response.setServerAddress(getServerAddress());
        response.setEventCount(dataHolder.getCommStatus().getEventCount());
        response.setStatus(dataHolder.getCommStatus().getCommStatus());
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        return true;
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        //no op
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.GET_COMM_EVENT_COUNTER.toInt();
    }
}
