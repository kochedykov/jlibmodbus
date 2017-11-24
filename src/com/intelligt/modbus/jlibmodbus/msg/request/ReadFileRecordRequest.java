package com.intelligt.modbus.jlibmodbus.msg.request;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusFileRecord;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadFileRecordResponse;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.ModbusFunctionCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
final public class ReadFileRecordRequest extends ModbusRequest {
    final static public int READ__SUB_REQ_LENGTH = 7;
    final List<ModbusFileRecord> records = new ArrayList<ModbusFileRecord>();

    public ReadFileRecordRequest() {
        super();
    }

    public void addFileRecord(ModbusFileRecord record) {
        this.records.add(record);
    }

    public void addFileRecords(Collection<ModbusFileRecord> records) {
        this.records.addAll(records);
    }

    @Override
    protected Class getResponseClass() {
        return ReadFileRecordResponse.class;
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        fifo.write(READ__SUB_REQ_LENGTH * records.size());
        for (ModbusFileRecord record : records) {
            fifo.write(ModbusFileRecord.REF_TYPE);
            fifo.writeShortBE(record.getFileNumber());
            fifo.writeShortBE(record.getRecordNumber());
            fifo.writeShortBE(record.getRecordLength());
        }
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        int byteCount = fifo.read();
        if (byteCount > (Modbus.MAX_PDU_LENGTH - 2))
            throw new ModbusNumberException("Byte count greater than max allowable");
        int read = 0;
        while (read < byteCount) {
            if (fifo.read() != ModbusFileRecord.REF_TYPE)
                throw new ModbusNumberException("Reference type mismatch.");
            int file_number = fifo.readShortBE();
            int record_number = fifo.readShortBE();
            int record_length = fifo.readShortBE();
            read += READ__SUB_REQ_LENGTH;
            records.add(new ModbusFileRecord(file_number, record_number, record_length));
        }
    }

    @Override
    public int requestSize() {
        return 1 + READ__SUB_REQ_LENGTH * records.size();
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        ReadFileRecordResponse response = (ReadFileRecordResponse) getResponse();
        try {
            for (ModbusFileRecord r : records) {
                dataHolder.readFileRecord(r);
            }
            /*
            TODO: it's required to reduce the number of data-copying-operations here
             */
            response.setFileRecords((ModbusFileRecord[]) records.toArray());
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    protected boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof ReadFileRecordResponse)) {
            return false;
        }
        ModbusFileRecord[] respRecords = ((ReadFileRecordResponse) response).getFileRecords();
        if (records.size() != respRecords.length)
            return false;
        for (int i = 0; i < records.size(); i++) {
            if (respRecords[i].getRecordLength() != records.get(i).getRecordLength())
                return false;
        }
        return true;
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.READ_FILE_RECORD.toInt();
    }
}
