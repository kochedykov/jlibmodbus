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
            throw new ModbusNumberException("Byte count greater then max allowable");
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
