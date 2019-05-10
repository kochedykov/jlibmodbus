package com.intelligt.modbus.jlibmodbus.data;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataAddressException;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;

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
 * since 1.2.8.4 it extends Observable to notify observers if register values was changed.
 *
 * @see java.util.Observable
 * @see java.util.Observer
 */
public class ModbusCoils extends ModbusValues<Boolean> {
    private boolean[] coils = new boolean[0];

    public ModbusCoils(int size) {
        this.coils = new boolean[Modbus.checkEndAddress(size) ? size : Modbus.MAX_START_ADDRESS];
    }

    public ModbusCoils(byte[] bytes) {
        coils = DataUtils.toBitsArray(bytes, bytes.length*8);
    }

    @Override
    public byte[] getBytes() {
        return DataUtils.toByteArray(coils);
    }

    @Override
    public void setBytesBe(byte[] bytes) {
        coils = DataUtils.toBitsArray(bytes, bytes.length*8);
    }

    public void setBytes(byte[] bytes, int quantity) {
        coils = DataUtils.toBitsArray(bytes, quantity);
    }

    void setSize(int size) {
        if (coils.length != size) {
            coils = Arrays.copyOf(coils, size);
        }
    }

    @Override
    public void setImpl(int offset, Boolean coil) throws IllegalDataAddressException, IllegalDataValueException {
        checkAddress(offset);
        synchronized (this) {
            coils[offset] = coil;
        }
    }

    public void setRange(int offset, boolean[] range) throws IllegalDataAddressException, IllegalDataValueException {
        checkRange(offset, range.length);
        if (!Modbus.checkWriteCoilCount(range.length))
            throw new IllegalDataValueException();
        synchronized (this) {
            for (int i = 0; i < range.length; i++) {
                set(offset + i, range[i]);
            }
        }
    }

    public boolean[] getRange(int offset, int quantity) throws IllegalDataAddressException, IllegalDataValueException {
        checkRange(offset, quantity);
        if (!Modbus.checkReadCoilCount(quantity))
            throw new IllegalDataValueException();
        synchronized (this) {
            return Arrays.copyOfRange(coils, offset, offset + quantity);
        }
    }

    @Override
    synchronized public int getQuantity() {
        return coils.length;
    }

    @Override
    synchronized public int getByteCount() {
        return (int)Math.ceil((double)coils.length/8);
    }

    @Override
    public Boolean get(int offset) throws IllegalDataAddressException {
        checkAddress(offset);
        synchronized (this) {
            return coils[offset];
        }
    }

    private void checkRange(int offset, int quantity) throws IllegalDataAddressException {
        if (offset + quantity > getQuantity())
            throw new IllegalDataAddressException(offset);
    }

    private void checkAddress(int offset) throws IllegalDataAddressException {
        if (!Modbus.checkStartAddress(offset))
            throw new IllegalDataAddressException(offset);
    }
}
