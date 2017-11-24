package com.intelligt.modbus.jlibmodbus.msg.base;

import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;

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
abstract public class AbstractMultipleRequest extends AbstractDataRequest {
    private int quantity;

    protected AbstractMultipleRequest() {
        super();
    }

    @Override
    protected void readData(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        setQuantity(fifo.readShortBE());
    }

    @Override
    protected void writeData(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(quantity);
    }

    @Override
    protected int dataSize() {
        return 2;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws ModbusNumberException {
        if (!checkAddressRange(getStartAddress(), quantity))
            throw new ModbusNumberException("End address out of bounds", getStartAddress() + quantity);
        this.quantity = quantity;
    }

    protected abstract boolean checkAddressRange(int startAddress, int quantity);
}
