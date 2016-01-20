package com.sbpinvertor.modbus.data.base;

import com.sbpinvertor.modbus.exception.ModbusDataException;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.utils.ByteFifo;

/**
 * Copyright (c) 2015 JSC "Zavod "Invertor"
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
abstract public class AbstractMultipleRequest extends AbstractDataRequest {
    private int quantity;

    public AbstractMultipleRequest(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        super(serverAddress, startAddress);
        if (!checkAddressRange(startAddress, quantity))
            throw new ModbusNumberException("Error in start address", startAddress);

        this.quantity = quantity;
    }

    public AbstractMultipleRequest(AbstractMultipleRequest msg) {
        super(msg);

        this.quantity = msg.quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    protected void readPDU(ByteFifo fifo) throws ModbusDataException {
        quantity = fifo.readShortBE();
    }

    @Override
    protected void writeData(ByteFifo fifo) throws ModbusDataException {
        fifo.writeShortBE(quantity);
    }

    abstract public boolean checkAddressRange(int startAddress, int quantity);
}
