package com.invertor.modbus.msg.response;

import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.AbstractWriteResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
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
final public class MaskWriteRegisterResponse extends AbstractWriteResponse {

    private int maskAnd;
    private int maskOr;

    public MaskWriteRegisterResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public MaskWriteRegisterResponse(int serverAddress, int startAddress, int maskAnd, int maskOr) throws ModbusNumberException {
        super(serverAddress, startAddress);

        setMaskAnd(maskAnd);
        setMaskOr(maskOr);
    }

    @Override
    protected void readValue(ModbusInputStream fifo) throws IOException {
        setMaskAnd(fifo.readShortBE());
        setMaskOr(fifo.readShortBE());
    }

    @Override
    protected void writeValue(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getMaskAnd());
        fifo.writeShortBE(getMaskOr());
    }


    @Override
    public int getFunction() {
        return ModbusFunctionCode.MASK_WRITE_REGISTER.toInt();
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
