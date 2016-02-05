package com.sbpinvertor.modbus.msg.response;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.msg.base.AbstractReadResponse;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.net.stream.base.ModbusOutputStream;
import com.sbpinvertor.modbus.utils.ModbusFunctionCode;

import java.io.IOException;

/**
 * Copyright (c) 2015-2016 JSC "Zavod "Invertor"
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
final public class ReportSlaveIdResponse extends AbstractReadResponse {

    private byte[] slaveId;

    public ReportSlaveIdResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public ReportSlaveIdResponse(int serverAddress, byte[] slaveId) throws ModbusNumberException {
        super(serverAddress, slaveId.length);

        this.slaveId = slaveId;
    }

    public byte[] getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(byte[] slaveId) {
        this.slaveId = slaveId;
    }

    @Override
    protected void readData(ModbusInputStream fifo) throws IOException {
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
    public ModbusFunctionCode getFunction() {
        return ModbusFunctionCode.REPORT_SLAVE_ID;
    }
}
