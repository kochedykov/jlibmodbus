package com.invertor.modbus.msg.request;

import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.ReadWriteMultipleRegistersResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
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
//facade
public class ReadWriteMultipleRegistersRequest extends ModbusRequest {

    final private ReadHoldingRegistersRequest reader;
    final private WriteMultipleRegistersRequest writer;

    public ReadWriteMultipleRegistersRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
        reader = new ReadHoldingRegistersRequest(serverAddress);
        writer = new WriteMultipleRegistersRequest(serverAddress);
    }

    public ReadWriteMultipleRegistersRequest(int serverAddress, int readAddress, int readQuantity, int writeAddress, int[] buffer) throws ModbusNumberException {
        super(serverAddress);
        reader = new ReadHoldingRegistersRequest(serverAddress, readAddress, readQuantity);
        writer = new WriteMultipleRegistersRequest(serverAddress, writeAddress, buffer);
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        reader.writeRequest(fifo);
        writer.writeRequest(fifo);
    }

    @Override
    public int requestSize() {
        return reader.requestSize() + writer.requestSize();
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        ReadWriteMultipleRegistersResponse response = new ReadWriteMultipleRegistersResponse(getServerAddress());
        try {
            dataHolder.writeHoldingRegisterRange(writer.getStartAddress(), writer.getRegisters());
            int[] range = dataHolder.readHoldingRegisterRange(reader.getStartAddress(), reader.getQuantity());
            response.setBuffer(range);
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof ReadWriteMultipleRegistersResponse)) {
            return false;
        }
        ReadWriteMultipleRegistersResponse r = (ReadWriteMultipleRegistersResponse) response;
        return r.getByteCount() == reader.getQuantity() * 2;
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        reader.readPDU(fifo);
        writer.readPDU(fifo);
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.READ_WRITE_MULTIPLE_REGISTERS.toInt();
    }
}
