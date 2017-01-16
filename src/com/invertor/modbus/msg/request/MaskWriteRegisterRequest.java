package com.invertor.modbus.msg.request;

import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.AbstractDataRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.MaskWriteRegisterResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
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
final public class MaskWriteRegisterRequest extends AbstractDataRequest {

    private int maskAnd;
    private int maskOr;

    public MaskWriteRegisterRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public MaskWriteRegisterRequest(int serverAddress, int startAddress, int maskAnd, int maskOr) throws ModbusNumberException {
        super(serverAddress, startAddress);

        setMaskAnd(maskAnd);
        setMaskOr(maskOr);
    }

    /*result = ((reg & and) | (or & !and))*/
    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        MaskWriteRegisterResponse response = new MaskWriteRegisterResponse(getServerAddress(), getStartAddress(), getMaskAnd(), getMaskOr());
        try {
            int reg = dataHolder.readHoldingRegister(getStartAddress());
            dataHolder.writeHoldingRegister(getStartAddress(), (reg & getMaskAnd()) | (getMaskOr() & (~getMaskAnd())));
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof MaskWriteRegisterResponse)) {
            return false;
        }
        MaskWriteRegisterResponse r = (MaskWriteRegisterResponse) response;
        return (r.getStartAddress() == getStartAddress()) &&
                (r.getMaskAnd() == getMaskAnd()) &&
                (r.getMaskOr() == getMaskOr());
    }

    @Override
    protected void readData(ModbusInputStream fifo) throws IOException {
        setMaskAnd(fifo.readShortBE());
        setMaskOr(fifo.readShortBE());
    }

    @Override
    public void writeData(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getMaskAnd());
        fifo.writeShortBE(getMaskOr());
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.MASK_WRITE_REGISTER.toInt();
    }

    @Override
    protected int dataSize() {
        return 4;
    }

    public int getMaskAnd() {
        return maskAnd;
    }

    public void setMaskAnd(int maskAnd) {
        this.maskAnd = maskAnd;
    }

    public int getMaskOr() {
        return maskOr;
    }

    public void setMaskOr(int maskOr) {
        this.maskOr = maskOr;
    }
}
