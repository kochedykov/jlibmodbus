package com.invertor.modbus.msg.request;

import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.base.mei.MEIFactory;
import com.invertor.modbus.msg.base.mei.ModbusEncapsulatedInterface;
import com.invertor.modbus.msg.response.EncapsulatedInterfaceTransportResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.utils.MEITypeCode;
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
public class EncapsulatedInterfaceTransportRequest extends ModbusRequest {

    private ModbusEncapsulatedInterface mei = null;

    public EncapsulatedInterfaceTransportRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
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
        EncapsulatedInterfaceTransportResponse response = new EncapsulatedInterfaceTransportResponse(getServerAddress());
        response.setMei(mei);
        try {
            response.getMei().process(dataHolder);
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
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
        if (!(response instanceof EncapsulatedInterfaceTransportResponse)) {
            return false;
        }
        return mei.getTypeCode() == ((EncapsulatedInterfaceTransportResponse) response).getMei().getTypeCode();
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.ENCAPSULATED_INTERFACE_TRANSPORT.toInt();
    }
}
