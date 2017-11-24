package com.intelligt.modbus.jlibmodbus.msg.response;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.base.AbstractReadResponse;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.ModbusFunctionCode;

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
final public class ReportSlaveIdResponse extends AbstractReadResponse {

    private byte[] slaveId;

    public ReportSlaveIdResponse() {
        super();
    }

    public byte[] getSlaveId() {
        return slaveId != null ? Arrays.copyOf(slaveId, slaveId.length) : new byte[0];
    }

    public void setSlaveId(byte[] slaveId) throws ModbusNumberException {
        if ((slaveId.length + 2) > Modbus.MAX_PDU_LENGTH)
            throw new ModbusNumberException("Slave Id greater than max pdu length: ", getByteCount());
        setByteCount(slaveId.length);
        this.slaveId = Arrays.copyOf(slaveId, slaveId.length);
    }

    @Override
    protected void readData(ModbusInputStream fifo) throws IOException {
        if (Modbus.MAX_PDU_LENGTH < responseSize()) {
            throw new IOException("Slave Id greater than max pdu length: " + getByteCount());
        }
        slaveId = new byte[getByteCount()];
        int size;
        if ((size = fifo.read(slaveId)) < slaveId.length)
            Modbus.log().warning(slaveId.length + " bytes expected, but " + size + " received.");
    }

    @Override
    protected void writeData(ModbusOutputStream fifo) throws IOException {
        fifo.write(slaveId);
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.REPORT_SLAVE_ID.toInt();
    }
}
