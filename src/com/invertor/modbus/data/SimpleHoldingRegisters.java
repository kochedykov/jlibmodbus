package com.invertor.modbus.data;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.IllegalDataAddressException;
import com.invertor.modbus.exception.IllegalDataValueException;

import java.util.Arrays;

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

/**
 * A simple implementation of register storage.
 */
public class SimpleHoldingRegisters extends HoldingRegisters {

    private int[] registers = new int[0];

    public SimpleHoldingRegisters(int size) {
        this.registers = new int[Modbus.checkEndAddress(size) ? size : Modbus.MAX_START_ADDRESS];
    }

    void setSize(int size) {
        synchronized (this) {
            if (registers.length != size) {
                registers = Arrays.copyOf(registers, size);
            }
        }
    }

    @Override
    public void set(int offset, int value) throws IllegalDataAddressException, IllegalDataValueException {
        checkAddress(offset);
        checkValue(value);
        synchronized (this) {
            registers[offset] = value;
        }
        /*
         * notify observers
         */
        super.set(offset, value);
    }

    @Override
    public void setRange(int offset, int[] range) throws IllegalDataAddressException, IllegalDataValueException {
        checkRange(offset, range.length);
        if (!Modbus.checkWriteRegisterCount(range.length))
            throw new IllegalDataAddressException(offset);
        synchronized (this) {
            System.arraycopy(range, 0, registers, offset, range.length);
        }
        /*
         * notify observers
         */
        super.setRange(offset, range);
    }

    @Override
    synchronized public int quantity() {
        return registers.length;
    }

    @Override
    public int get(int offset) throws IllegalDataAddressException {
        checkAddress(offset);
        synchronized (this) {
            return registers[offset];
        }
    }

    @Override
    public int[] getRange(int offset, int quantity) throws IllegalDataAddressException, IllegalDataValueException {
        checkRange(offset, quantity);
        if (!Modbus.checkReadRegisterCount(quantity))
            throw new IllegalDataAddressException(offset);
        synchronized (this) {
            return Arrays.copyOfRange(registers, offset, offset + quantity);
        }
    }

    private void checkRange(int offset, int quantity) throws IllegalDataAddressException {
        if (offset + quantity > quantity())
            throw new IllegalDataAddressException(offset);
    }

    private void checkAddress(int offset) throws IllegalDataAddressException {
        if (offset > (quantity() - 1))
            throw new IllegalDataAddressException(offset);
    }

    private void checkValue(int value) throws IllegalDataValueException {
        if (!Modbus.checkRegisterValue(value))
            throw new IllegalDataValueException();
    }

    /*
    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                if (index >= registers.length)
                    throw new ConcurrentModificationException();
                return (index+1) < registers.length;
            }

            @Override
            public Integer next() {
                if (hasNext()) {
                    return registers[++index];
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {

            }
        };
    }
    */
}
