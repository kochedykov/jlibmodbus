package com.intelligt.modbus.jlibmodbus.msg.response;

import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.base.mei.MEIFactory;
import com.intelligt.modbus.jlibmodbus.msg.base.mei.ModbusEncapsulatedInterface;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.MEITypeCode;
import com.intelligt.modbus.jlibmodbus.utils.ModbusFunctionCode;

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
public class EncapsulatedInterfaceTransportResponse extends ModbusResponse {

    private ModbusEncapsulatedInterface mei = null;

    public EncapsulatedInterfaceTransportResponse() {
        super();
    }

    @Override
    protected void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        int meiTypeCode = fifo.read();
        setMei(MEIFactory.getMEI(MEITypeCode.get(meiTypeCode)));
        if (getMei() == null)
            throw new ModbusNumberException("Unknown MEI type", meiTypeCode);
        getMei().readResponse(fifo);
    }

    @Override
    protected void writeResponse(ModbusOutputStream fifo) throws IOException {
        fifo.write(getMei().getTypeCode().toInt());
        getMei().writeResponse(fifo);
    }

    public ModbusEncapsulatedInterface getMei() {
        return mei;
    }

    public void setMei(ModbusEncapsulatedInterface mei) {
        this.mei = mei;
    }

    @Override
    protected int responseSize() {
        return 1 + (mei == null ? 0 : mei.getResponseSize());
    }

    public int getFunction() {
        return ModbusFunctionCode.ENCAPSULATED_INTERFACE_TRANSPORT.toInt();
    }
}
