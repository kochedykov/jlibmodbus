package com.invertor.modbus.msg.response;

import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.AbstractReadResponse;
import com.invertor.modbus.msg.base.ModbusFileRecord;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.utils.DataUtils;
import com.invertor.modbus.utils.ModbusFunctionCode;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
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
final public class ReadFileRecordResponse extends AbstractReadResponse {

    public static final int READ_RESP_SUB_REQ_LENGTH = 2;
    private ModbusFileRecord[] fileRecords = null;

    public ReadFileRecordResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    @Override
    protected void readData(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        List<ModbusFileRecord> records = new LinkedList<ModbusFileRecord>();
        int response_byte_count = getByteCount();
        int read = 0;
        int i = 0;
        while (read < response_byte_count) {
            int record_byte_count = fifo.read() * 2;
            if (fifo.read() != ModbusFileRecord.REF_TYPE)
                throw new ModbusNumberException("Reference type mismatch.");
            read += READ_RESP_SUB_REQ_LENGTH;
            if (record_byte_count > response_byte_count - read) {
                record_byte_count = response_byte_count - read;
            }
            byte[] buffer = new byte[record_byte_count];
            if (fifo.read(buffer) != record_byte_count)
                throw new ModbusNumberException(record_byte_count + " bytes expected, but not received.");
            read += record_byte_count;
            ModbusFileRecord mfr = new ModbusFileRecord(0, i++, DataUtils.toIntArray(buffer));
            records.add(mfr);
        }
        setFileRecords(records.toArray(new ModbusFileRecord[records.size()]));
    }

    @Override
    protected void writeData(ModbusOutputStream fifo) throws IOException {
        for (ModbusFileRecord r : getFileRecords()) {
            fifo.write(r.getRecordLength());
            fifo.write(ModbusFileRecord.REF_TYPE);
            fifo.write(DataUtils.toByteArray(r.getRegisters()));
        }
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.READ_FILE_RECORD.toInt();
    }

    public ModbusFileRecord[] getFileRecords() {
        return Arrays.copyOf(fileRecords, fileRecords.length);
    }

    public void setFileRecords(ModbusFileRecord[] fileRecords) throws ModbusNumberException {
        this.fileRecords = Arrays.copyOf(fileRecords, fileRecords.length);
        int byteCount = 0;
        for (ModbusFileRecord r : getFileRecords()) {
            byteCount += 2 + r.getRecordLength() * 2;
        }
        setByteCount(byteCount);
    }
}
