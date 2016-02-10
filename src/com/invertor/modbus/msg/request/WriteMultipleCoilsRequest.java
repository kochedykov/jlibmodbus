package com.invertor.modbus.msg.request;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.AbstractWriteMultipleRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.WriteMultipleCoilsResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

final public class WriteMultipleCoilsRequest extends AbstractWriteMultipleRequest {

    private boolean[] coils = null;

    public WriteMultipleCoilsRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public WriteMultipleCoilsRequest(int serverAddress, int startAddress, boolean[] coils) throws ModbusNumberException {
        super(serverAddress, startAddress, DataUtils.toByteArray(coils), coils.length);
        setCoils(coils);
    }

    @Override
    protected boolean checkAddressRange(int startAddress, int quantity) {
        return Modbus.checkWriteCoilCount(quantity) &&
                Modbus.checkStartAddress(startAddress) &&
                Modbus.checkEndAddress(startAddress + quantity);
    }

    @Override
    public ModbusResponse getResponse(DataHolder dataHolder) throws ModbusNumberException {
        WriteMultipleCoilsResponse response = new WriteMultipleCoilsResponse(getServerAddress(), getStartAddress(), getQuantity());
        try {
            dataHolder.writeCoilRange(getStartAddress(), getCoils());
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        WriteMultipleCoilsResponse r = (WriteMultipleCoilsResponse) response;
        return r.getStartAddress() == getStartAddress() && r.getValue() == getQuantity();
    }

    @Override
    public void readData(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        super.readData(fifo);
        if (Math.ceil((double) getQuantity() / 8) != getByteCount()) {
            throw new ModbusNumberException("Byte count not matches quantity/8", getByteCount());
        }
        if (!checkAddressRange(getStartAddress(), getQuantity()))
            throw new ModbusNumberException("Coil count greater then max coil count", getQuantity());
        setCoils(DataUtils.toBitsArray(getValues(), getQuantity()));
    }

    private boolean[] getCoils() {
        return coils;
    }

    private void setCoils(boolean[] coils) {
        this.coils = coils;
    }

    @Override
    public ModbusFunctionCode getFunction() {
        return ModbusFunctionCode.WRITE_MULTIPLE_COILS;
    }
}
