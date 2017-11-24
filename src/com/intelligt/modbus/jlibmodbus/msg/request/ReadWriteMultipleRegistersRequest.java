package com.intelligt.modbus.jlibmodbus.msg.request;

import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadWriteMultipleRegistersResponse;
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
//facade
public class ReadWriteMultipleRegistersRequest extends ModbusRequest {

    final private ReadHoldingRegistersRequest reader;
    final private WriteMultipleRegistersRequest writer;

    public ReadWriteMultipleRegistersRequest() {
        super();
        reader = new ReadHoldingRegistersRequest();
        writer = new WriteMultipleRegistersRequest();
    }

    @Override
    protected Class getResponseClass() {
        return ReadWriteMultipleRegistersResponse.class;
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
        ReadWriteMultipleRegistersResponse response = (ReadWriteMultipleRegistersResponse) getResponse();
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

    @Override
    public void setServerAddress(int serverAddress) throws ModbusNumberException {
        super.setServerAddress(serverAddress);

        reader.setServerAddress(serverAddress);
        writer.setServerAddress(serverAddress);
    }

    public int getReadAddress() {
        return reader.getStartAddress();
    }

    /*
    facade
     */
    public void setReadAddress(int address) throws ModbusNumberException {
        reader.setStartAddress(address);
    }

    public int getReadQuantity() {
        return reader.getQuantity();
    }

    public void setReadQuantity(int quantity) throws ModbusNumberException {
        reader.setQuantity(quantity);
    }

    public void setWriteAddress(int address) throws ModbusNumberException {
        writer.setStartAddress(address);
    }

    public void setWriteRegisters(int[] registers) throws ModbusNumberException {
        writer.setRegisters(registers);
    }
}
