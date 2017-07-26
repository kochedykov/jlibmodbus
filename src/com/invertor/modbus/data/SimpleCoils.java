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
 * A simple implementation of coil storage.
 */
public class SimpleCoils extends Coils {

    private boolean[] coils = new boolean[0];

    public SimpleCoils(int size) {
        this.coils = new boolean[Modbus.checkEndAddress(size) ? size : Modbus.MAX_START_ADDRESS];
    }

    void setSize(int size) {
        if (coils.length != size) {
            coils = Arrays.copyOf(coils, size);
        }
    }

    @Override
    public void set(int offset, boolean coil) throws IllegalDataAddressException {
        checkAddress(offset);
        synchronized (this) {
            coils[offset] = coil;
        }
        /*
         * notify observers
         */
        super.set(offset, coil);
    }

    @Override
    public void setRange(int offset, boolean[] range) throws IllegalDataAddressException, IllegalDataValueException {
        checkRange(offset, range.length);
        if (!Modbus.checkWriteCoilCount(range.length))
            throw new IllegalDataValueException();
        synchronized (this) {
            System.arraycopy(range, 0, coils, offset, range.length);
        }
        /*
         * notify observers
         */
        super.setRange(offset, range);
    }

    @Override
    synchronized public int quantity() {
        return coils.length;
    }

    @Override
    public boolean get(int offset) throws IllegalDataAddressException {
        checkAddress(offset);
        synchronized (this) {
            return coils[offset];
        }
    }

    @Override
    public boolean[] getRange(int offset, int quantity) throws IllegalDataAddressException, IllegalDataValueException {
        checkRange(offset, quantity);
        if (!Modbus.checkReadCoilCount(quantity))
            throw new IllegalDataValueException();
        synchronized (this) {
            return Arrays.copyOfRange(coils, offset, offset + quantity);
        }
    }

    private void checkRange(int offset, int quantity) throws IllegalDataAddressException {
        if (offset + quantity > quantity())
            throw new IllegalDataAddressException(offset);
    }

    private void checkAddress(int offset) throws IllegalDataAddressException {
        if (!Modbus.checkStartAddress(offset))
            throw new IllegalDataAddressException(offset);
    }
}
