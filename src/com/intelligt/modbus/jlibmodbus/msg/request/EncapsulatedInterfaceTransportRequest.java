package com.intelligt.modbus.jlibmodbus.msg.request;

import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.base.mei.MEIFactory;
import com.intelligt.modbus.jlibmodbus.msg.base.mei.ModbusEncapsulatedInterface;
import com.intelligt.modbus.jlibmodbus.msg.response.EncapsulatedInterfaceTransportResponse;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.MEITypeCode;
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
public class EncapsulatedInterfaceTransportRequest extends ModbusRequest {

    private ModbusEncapsulatedInterface mei = null;

    public EncapsulatedInterfaceTransportRequest() {
        super();
    }

    @Override
    protected Class getResponseClass() {
        return EncapsulatedInterfaceTransportResponse.class;
    }

    public void setMEIType(MEITypeCode meiTypeCode) {
        setMei(MEIFactory.getMEI(meiTypeCode));
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        fifo.write(mei.getTypeCode().toInt());
        mei.writeRequest(fifo);
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        int meiTypeCode = fifo.read();
        if ((mei = MEIFactory.getMEI(MEITypeCode.get(meiTypeCode))) == null) {
            throw new ModbusNumberException("Unknown MEI type", meiTypeCode);
        }
        mei.readRequest(fifo);
    }

    @Override
    public int requestSize() {
        return 1 + (mei == null ? 0 : mei.getRequestSize());
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        EncapsulatedInterfaceTransportResponse response = new EncapsulatedInterfaceTransportResponse();
        response.setServerAddress(getServerAddress());
        mei.process(dataHolder);
        response.setMei(mei);
        return response;
    }

    public ModbusEncapsulatedInterface getMei() {
        return mei;
    }

    public void setMei(ModbusEncapsulatedInterface mei) {
        this.mei = mei;
    }

    @Override
    protected boolean validateResponseImpl(ModbusResponse response) {
        return response instanceof EncapsulatedInterfaceTransportResponse && mei.getTypeCode() == ((EncapsulatedInterfaceTransportResponse) response).getMei().getTypeCode();
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.ENCAPSULATED_INTERFACE_TRANSPORT.toInt();
    }
}
