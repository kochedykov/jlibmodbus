package com.invertor.modbus.msg.response;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ReadFifoQueueResponse extends ModbusResponse {

    private int[] fifoValueRegister;

    public ReadFifoQueueResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    private void checkFifoCount(int fifoCount) throws ModbusNumberException {
        if (fifoCount > Modbus.MAX_FIFO_COUNT)
            throw new ModbusNumberException("Fifo count greater then max fifo count", fifoCount);
    }

    @Override
    protected void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        int byteCount = fifo.readShortBE();
        int fifoCount = fifo.readShortBE();
        if ((fifoCount * 2) != byteCount)
            throw new ModbusNumberException("Fifo count not matches bytes*2", fifoCount);
        checkFifoCount(fifoCount);
        byte[] bytes = new byte[byteCount];
        if (fifo.read(bytes) != byteCount) {
            throw new IOException("Can't read fifo value register");
        }
        setFifoValueRegister(DataUtils.toIntArray(bytes));
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
        return 2 + 2 + fifoValueRegister.length * 2;
    }

    @Override
    public ModbusFunctionCode getFunction() {
        return ModbusFunctionCode.READ_FIFO_QUEUE;
    }

    public int[] getFifoValueRegister() {
        return fifoValueRegister;
    }

    public void setFifoValueRegister(int[] fifoValueRegister) throws ModbusNumberException {
        checkFifoCount(fifoValueRegister.length);
        this.fifoValueRegister = fifoValueRegister;
    }
}
