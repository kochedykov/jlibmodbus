package com.intelligt.modbus.jlibmodbus.msg.response;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;
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
public class ReadFifoQueueResponse extends ModbusResponse {

    private byte[] bytes;

    public ReadFifoQueueResponse() {
        super();
    }

    private void checkFifoCount(int fifoCount) throws ModbusNumberException {
        if (fifoCount > Modbus.MAX_FIFO_COUNT)
            throw new ModbusNumberException("Fifo count greater than max fifo count", fifoCount);
    }

    @Override
    protected void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        int byteCount = fifo.readShortBE();
        int fifoCount = fifo.readShortBE();
        if ((fifoCount * 2) != byteCount)
            throw new ModbusNumberException("Fifo count not matches bytes*2", fifoCount);
        checkFifoCount(fifoCount);
        bytes = new byte[byteCount];
        if (fifo.read(bytes) != byteCount) {
            throw new IOException("Can't read fifo value register");
        }
    }

    @Override
    protected void writeResponse(ModbusOutputStream fifo) throws IOException {
        int[] r = getFifoValueRegister();
        fifo.writeShortBE(r.length * 2);
        fifo.writeShortBE(r.length);
        fifo.write(DataUtils.toByteArray(r));
    }

    @Override
    protected int responseSize() {
        return 2 + 2 + (bytes != null ? bytes.length : 0);
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.READ_FIFO_QUEUE.toInt();
    }

    public int[] getFifoValueRegister() {
        return DataUtils.BeToIntArray(bytes);
    }

    public void setFifoValueRegister(int[] fifoValueRegister) throws ModbusNumberException {
        checkFifoCount(fifoValueRegister.length);
        this.bytes = DataUtils.toByteArray(fifoValueRegister);
    }
}
