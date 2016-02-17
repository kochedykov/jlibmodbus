package com.invertor.modbus.msg.request;

import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.base.mei.MEIFactory;
import com.invertor.modbus.msg.base.mei.ModbusEncapsulatedInterface;
import com.invertor.modbus.msg.response.EncapsulatedInterfaceTransportResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.utils.MEITypeCode;
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
public class EncapsulatedInterfaceTransportRequest extends ModbusRequest {

    private ModbusEncapsulatedInterface mei = null;

    public EncapsulatedInterfaceTransportRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public void setMEIType(MEITypeCode meiTypeCode) {
        setMei(MEIFactory.getMEI(meiTypeCode));
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        fifo.write(mei.getTypeCode().toInt());
        mei.writeRequest(fifo);
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        int meiTypeCode = fifo.read();
        if ((mei = MEIFactory.getMEI(MEITypeCode.get(meiTypeCode))) == null) {
            throw new ModbusNumberException("Unknown MEI type", meiTypeCode);
        }
        mei.readRequest(fifo);
    }

    @Override
    public int requestSize() {
        return 1 + (mei == null ? 0 : mei.getRequestSize());
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        EncapsulatedInterfaceTransportResponse response = new EncapsulatedInterfaceTransportResponse(getServerAddress());
        response.setMei(mei);
        try {
            response.getMei().process(dataHolder);
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    public ModbusEncapsulatedInterface getMei() {
        return mei;
    }

    public void setMei(ModbusEncapsulatedInterface mei) {
        this.mei = mei;
    }

    @Override
    protected boolean validateResponseImpl(ModbusResponse response) {
        return mei.getTypeCode() == ((EncapsulatedInterfaceTransportResponse) response).getMei().getTypeCode();
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.ENCAPSULATED_INTERFACE_TRANSPORT.toInt();
    }
}
