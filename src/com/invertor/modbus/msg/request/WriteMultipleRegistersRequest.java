package com.invertor.modbus.msg.request;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.AbstractWriteMultipleRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.WriteMultipleRegistersResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.utils.DataUtils;
import com.invertor.modbus.utils.ModbusFunctionCode;

import java.io.IOException;
import java.util.Arrays;

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

final public class WriteMultipleRegistersRequest extends AbstractWriteMultipleRequest {

    public WriteMultipleRegistersRequest() {
        super();
    }

    @Override
    protected Class getResponseClass() {
        return WriteMultipleRegistersResponse.class;
    }

    @Override
    public void setByteCount(int byteCount) throws ModbusNumberException {
        if (byteCount % 2 != 0) {
            throw new ModbusNumberException("bytes.length=" + byteCount + " should be an even number");
        }
        super.setByteCount(byteCount);
        setQuantity(byteCount / 2);
    }

    @Override
    protected boolean checkAddressRange(int startAddress, int quantity) {
        return Modbus.checkWriteRegisterCount(quantity) &&
                Modbus.checkStartAddress(startAddress) &&
                Modbus.checkEndAddress(startAddress + quantity);
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        WriteMultipleRegistersResponse response = (WriteMultipleRegistersResponse) getResponse();
        response.setStartAddress(getStartAddress());
        response.setQuantity(getQuantity());
        try {
            dataHolder.writeHoldingRegisterRange(getStartAddress(), getRegisters());
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof WriteMultipleRegistersResponse)) {
            return false;
        }
        WriteMultipleRegistersResponse r = (WriteMultipleRegistersResponse) response;
        return r.getStartAddress() == getStartAddress() && r.getValue() == getQuantity();
    }

    @Override
    public void readData(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        super.readData(fifo);
        if (getQuantity() * 2 != getByteCount()) {
            throw new ModbusNumberException("Byte count not matches quantity*2", getByteCount());
        }
        if (!checkAddressRange(getStartAddress(), getQuantity()))
            throw new ModbusNumberException("Register count greater than max register count", getQuantity());
    }

    public int[] getRegisters() {
        return DataUtils.toIntArray(getValues());
    }

    public void setRegisters(int[] registers) throws ModbusNumberException {
        super.setValues(DataUtils.toByteArray(registers));
        setByteCount(registers.length * 2);
    }

    @Override
    public byte[] getValues() {
        return Arrays.copyOf(super.getValues(), super.getByteCount());
    }

    @Override
    public void setValues(byte[] values) throws ModbusNumberException {
        super.setValues(Arrays.copyOf(values, values.length));
        setByteCount(values.length);
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.WRITE_MULTIPLE_REGISTERS.toInt();
    }
}
