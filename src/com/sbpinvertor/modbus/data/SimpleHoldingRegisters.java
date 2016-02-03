package com.sbpinvertor.modbus.data;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.exception.IllegalDataAddressException;
import com.sbpinvertor.modbus.exception.IllegalDataValueException;

import java.util.Arrays;

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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class SimpleHoldingRegisters implements HoldingRegisters {

    final private int[] registers;

    public SimpleHoldingRegisters(int size) {
        this.registers = new int[Modbus.checkStartAddress(size) ? size : Modbus.MAX_START_ADDRESS];
    }

    @Override
    public void set(int offset, int value) throws IllegalDataAddressException, IllegalDataValueException {
        checkAddress(offset);
        checkValue(value);
        registers[offset] = value;
    }

    @Override
    public void setRange(int offset, int[] range) throws IllegalDataAddressException, IllegalDataValueException {
        //checkRange(offset, range.length);
        for (int i = 0; i < range.length; i++)
            set(offset + i, range[i]);
    }

    @Override
    public int get(int offset) throws IllegalDataAddressException {
        checkAddress(offset);
        return registers[offset];
    }

    @Override
    public int[] getRange(int offset, int quantity) throws IllegalDataAddressException {
        checkRange(offset, quantity);
        return Arrays.copyOfRange(registers, offset, offset + quantity);
    }

    private void checkRange(int offset, int quantity) throws IllegalDataAddressException {
        if (!Modbus.checkStartAddress(offset) || !Modbus.checkEndAddress(offset + quantity))
            throw new IllegalDataAddressException(offset);
    }

    private void checkAddress(int offset) throws IllegalDataAddressException {
        if (!Modbus.checkStartAddress(offset))
            throw new IllegalDataAddressException(offset);
    }

    private void checkValue(int value) throws IllegalDataValueException {
        if (!Modbus.checkRegisterValue(value))
            throw new IllegalDataValueException(value);
    }
}
