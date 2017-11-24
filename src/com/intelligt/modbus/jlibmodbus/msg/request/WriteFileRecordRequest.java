package com.intelligt.modbus.jlibmodbus.msg.request;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusFileRecord;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.WriteFileRecordResponse;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;
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
final public class WriteFileRecordRequest extends ModbusRequest {

    final static public int READ_SUB_REQ_LENGTH = 7;
    private ModbusFileRecord fileRecord = null;

    public WriteFileRecordRequest() {
        super();
    }

    public ModbusFileRecord getFileRecord() {
        return fileRecord;
    }

    public void setFileRecord(ModbusFileRecord record) {
        this.fileRecord = record;
    }

    @Override
    protected Class getResponseClass() {
        return WriteFileRecordResponse.class;
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        ModbusFileRecord record = getFileRecord();
        fifo.write(READ_SUB_REQ_LENGTH + record.getRecordLength() * 2);
        fifo.write(ModbusFileRecord.REF_TYPE);
        fifo.writeShortBE(record.getFileNumber());
        fifo.writeShortBE(record.getRecordNumber());
        fifo.writeShortBE(record.getRecordLength());
        fifo.write(DataUtils.toByteArray(record.getRegisters()));
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        int byteCount = fifo.read();
        if (byteCount > (Modbus.MAX_PDU_LENGTH - 2))
            throw new ModbusNumberException("Byte count greater than max allowable");
        if (fifo.read() != ModbusFileRecord.REF_TYPE)
            throw new ModbusNumberException("Reference type mismatch.");
        int file_number = fifo.readShortBE();
        int record_number = fifo.readShortBE();
        int record_length = fifo.readShortBE();
        byte[] buffer = new byte[record_length * 2];
        if (fifo.read(buffer) != buffer.length)
            throw new ModbusNumberException(record_length + " bytes expected, but not received.");
        setFileRecord(new ModbusFileRecord(file_number, record_number, DataUtils.BeToIntArray(buffer)));
    }

    @Override
    public int requestSize() {
        return 1 + READ_SUB_REQ_LENGTH * getFileRecord().getRecordLength() * 2;
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        WriteFileRecordResponse response = (WriteFileRecordResponse) getResponse();
        response.setFileRecord(getFileRecord());
        try {
            dataHolder.writeFileRecord(response.getFileRecord());
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    protected boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof WriteFileRecordResponse)) {
            return false;
        }
        ModbusFileRecord reqRecord = getFileRecord();
        ModbusFileRecord respRecord = ((WriteFileRecordResponse) response).getFileRecord();
        return !(respRecord.getFileNumber() != reqRecord.getFileNumber() ||
                respRecord.getRecordNumber() != reqRecord.getRecordNumber() ||
                respRecord.getRecordLength() != reqRecord.getRecordLength());
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.WRITE_FILE_RECORD.toInt();
    }
}
