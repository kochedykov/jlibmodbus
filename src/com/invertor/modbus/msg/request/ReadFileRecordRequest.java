package com.invertor.modbus.msg.request;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.ModbusFileRecord;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.ReadFileRecordResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.utils.ModbusFunctionCode;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
final public class ReadFileRecordRequest extends ModbusRequest {

    final static public int READ__SUB_REQ_LENGTH = 7;
    private ModbusFileRecord[] records = null;

    public ReadFileRecordRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public ReadFileRecordRequest(int serverAddress, ModbusFileRecord[] records) throws ModbusNumberException {
        super(serverAddress);
        this.records = records;
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        fifo.write(READ__SUB_REQ_LENGTH * records.length);
        for (ModbusFileRecord record : records) {
            fifo.write(ModbusFileRecord.REF_TYPE);
            fifo.writeShortBE(record.getFileNumber());
            fifo.writeShortBE(record.getRecordNumber());
            fifo.writeShortBE(record.getRecordLength());
        }
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        List<ModbusFileRecord> records = new LinkedList<ModbusFileRecord>();
        int byteCount = fifo.read();
        if (byteCount > (Modbus.MAX_PDU_LENGTH - 2))
            throw new ModbusNumberException("Byte count greater then max allowable");
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
        this.records = records.toArray(new ModbusFileRecord[records.size()]);
    }

    @Override
    public int requestSize() {
        return 1 + READ__SUB_REQ_LENGTH * records.length;
    }

    @Override
    public ModbusResponse getResponse(DataHolder dataHolder) throws ModbusNumberException {
        ReadFileRecordResponse response = new ReadFileRecordResponse(getServerAddress());
        try {
            for (ModbusFileRecord r : records) {
                r.writeRegisters(dataHolder.readFileRecord(r.getFileNumber(), r.getRecordNumber(), r.getRecordLength()));
            }
            response.setFileRecords(records);
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    protected boolean validateResponseImpl(ModbusResponse response) {
        ModbusFileRecord[] respRecords = ((ReadFileRecordResponse) response).getFileRecords();
        if (records.length != respRecords.length)
            return false;
        for (int i = 0; i < records.length; i++) {
            if (respRecords[i].getRecordLength() != records[i].getRecordLength())
                return false;
        }
        return true;
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.READ_FILE_RECORD.toInt();
    }
}
