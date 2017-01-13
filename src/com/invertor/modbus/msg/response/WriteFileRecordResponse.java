package com.invertor.modbus.msg.response;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.ModbusFileRecord;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.utils.DataUtils;
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
final public class WriteFileRecordResponse extends ModbusResponse {

    final static public int READ_SUB_REQ_LENGTH = 7;
    private ModbusFileRecord record = null;

    public WriteFileRecordResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public WriteFileRecordResponse(int serverAddress, ModbusFileRecord record) throws ModbusNumberException {
        super(serverAddress);
        setFileRecord(record);
    }

    @Override
    protected void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
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
        record = new ModbusFileRecord(file_number, record_number, DataUtils.toIntArray(buffer));
    }

    @Override
    protected void writeResponse(ModbusOutputStream fifo) throws IOException {
        fifo.write(READ_SUB_REQ_LENGTH + record.getRecordLength() * 2);
        fifo.write(ModbusFileRecord.REF_TYPE);
        fifo.writeShortBE(record.getFileNumber());
        fifo.writeShortBE(record.getRecordNumber());
        fifo.writeShortBE(record.getRecordLength());
        fifo.write(DataUtils.toByteArray(record.getRegisters()));
    }

    public ModbusFileRecord getFileRecord() {
        return record;
    }

    public void setFileRecord(ModbusFileRecord record) {
        this.record = record;
    }

    @Override
    protected int responseSize() {
        return 1 + READ_SUB_REQ_LENGTH * record.getRecordLength() * 2;
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.WRITE_FILE_RECORD.toInt();
    }
}
