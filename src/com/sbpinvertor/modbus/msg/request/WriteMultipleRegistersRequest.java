package com.sbpinvertor.modbus.msg.request;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.ModbusFunction;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.msg.base.AbstractWriteMultipleRequest;
import com.sbpinvertor.modbus.net.streaming.base.ModbusInputStream;
import com.sbpinvertor.modbus.utils.DataUtils;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

final public class WriteMultipleRegistersRequest extends AbstractWriteMultipleRequest {

    public WriteMultipleRegistersRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    public WriteMultipleRegistersRequest(int serverAddress, int startAddress, int[] registers) throws ModbusNumberException {
        super(serverAddress, startAddress, DataUtils.toByteArray(registers), registers.length);
    }

    @Override
    public boolean checkAddressRange(int startAddress, int quantity) {
        return Modbus.checkReadRegisterCount(quantity) &&
                Modbus.checkStartAddress(startAddress) &&
                Modbus.checkEndAddress(startAddress + quantity);
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        super.readPDU(fifo);
        if (getQuantity() * 2 != getByteCount()) {
            throw new ModbusNumberException("Byte count not matches quantity*2", getByteCount());
        }
    }

    @Override
    public ModbusFunction getFunction() {
        return ModbusFunction.WRITE_MULTIPLE_REGISTERS;
    }
}
