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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
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
            response.setRegisters(range);
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
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
