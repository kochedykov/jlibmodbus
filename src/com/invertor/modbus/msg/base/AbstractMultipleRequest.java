package com.invertor.modbus.msg.base;

import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;

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

    protected AbstractMultipleRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    protected AbstractMultipleRequest(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        super(serverAddress, startAddress);
        if (!checkAddressRange(startAddress, quantity))
            throw new ModbusNumberException("Error in start address", startAddress);

        setQuantity(quantity);
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    protected abstract boolean checkAddressRange(int startAddress, int quantity);
}
