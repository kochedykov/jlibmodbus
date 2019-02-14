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
public class ModbusHoldingRegisters extends ModbusValues<Integer> {

    private int[] registers = new int[0];

    public ModbusHoldingRegisters(int size) {
        registers = new int[(Modbus.checkEndAddress(size) ? size : Modbus.MAX_START_ADDRESS)];
    }

    public ModbusHoldingRegisters() {

    }

    void setSize(int size) {
        synchronized (this) {
            if (registers.length != size) {
                registers = Arrays.copyOf(registers, size);
            }
        }
    }

    protected void checkOffsetAndValue(int offset, int value) throws IllegalDataValueException, IllegalDataAddressException {
        checkAddress(offset);
        checkValue(value);
    }

    @Override
    public void setImpl(int offset, Integer value) throws IllegalDataAddressException, IllegalDataValueException {
        checkAddress(offset);
        checkValue(value);
        synchronized (this) {
            registers[offset] = value;
        }
    }

    public void setRange(int offset, int[] range) throws IllegalDataAddressException, IllegalDataValueException {
        checkRange(offset, range.length);
        if (!Modbus.checkWriteRegisterCount(range.length))
            throw new IllegalDataAddressException(offset);
        synchronized (this) {
            for (int i = 0; i < range.length; i++) {
                set(offset + i, range[i]);
            }
        }
    }

    public void setInt8At(int offset, int i8) throws IllegalDataAddressException, IllegalDataValueException {
        checkOffsetAndValue(offset, i8);
        set(offset, (int) DataUtils.byteLow(i8));
    }

    public void setInt16At(int offset, int i16) throws IllegalDataAddressException, IllegalDataValueException {
        checkOffsetAndValue(offset, i16);
        set(offset, i16);
    }

    public void setInt32At(int offset, int i32) throws IllegalDataAddressException, IllegalDataValueException {
        setInt16At(offset, DataUtils.wordLow(i32));
        setInt16At(offset + 1, DataUtils.wordHigh(i32));
    }

    public void setInt64At(int offset, long i64) throws IllegalDataAddressException, IllegalDataValueException {
        setInt32At(offset, (int) (i64 & 0xffffffff));
        setInt32At(offset + 2, (int) ((i64 >> 32) & 0xffffffff));
    }

    public void setFloat32At(int offset, float f32) throws IllegalDataAddressException, IllegalDataValueException {
        setInt32At(offset, Float.floatToRawIntBits(f32));
    }

    public void setFloat64At(int offset, double f64) throws IllegalDataAddressException, IllegalDataValueException {
        setInt64At(offset, Double.doubleToRawLongBits(f64));
    }

    public int getInt8At(int offset) throws IllegalDataAddressException {
        checkAddress(offset);
        return DataUtils.byteLow(get(offset));
    }

    public int getInt16At(int offset) throws IllegalDataAddressException {
        return get(offset);
    }

    public int getInt32At(int offset) throws IllegalDataAddressException {
        return (getInt16At(offset)&0xffff) | ((getInt16At(offset + 1)&0xffff) << 16);
    }

    public long getInt64At(int offset) throws IllegalDataAddressException {
        return (getInt32At(offset)&0xffffffffL) | ((getInt32At(offset + 2)&0xffffffffL) << 32);
    }

    public float getFloat32At(int offset) throws IllegalDataAddressException {
        return Float.intBitsToFloat(getInt32At(offset));
    }

    public double getFloat64At(int offset) throws IllegalDataAddressException {
        return Double.longBitsToDouble(getInt64At(offset));
    }

    public int[] getRegisters() {
        return Arrays.copyOf(registers, getQuantity());
    }

    public void setRegisters(int[] registers) {
        this.registers = Arrays.copyOf(registers, registers.length);
    }

    @Override
    synchronized public int getByteCount() {
        return getQuantity() * 2;
    }

    @Override
    public byte[] getBytes() {
        return DataUtils.toByteArray(registers);
    }

    @Override
    public void setBytesBe(byte[] bytes) {
        registers = DataUtils.BeToIntArray(bytes);
    }

    public void setBytesLe(byte[] bytes) {
        this.registers = DataUtils.LeToIntArray(bytes);
    }

    @Override
    synchronized public int getQuantity() {
        return registers.length;
    }

    @Override
    public Integer get(int offset) throws IllegalDataAddressException {
        checkAddress(offset);
        synchronized (this) {
            return registers[offset];
        }
    }

    public int[] getRange(int offset, int quantity) throws IllegalDataAddressException {
        checkRange(offset, quantity);
        if (!Modbus.checkReadRegisterCount(quantity))
            throw new IllegalDataAddressException(offset);
        synchronized (this) {
            return Arrays.copyOfRange(registers, offset, offset + quantity);
        }
    }

    private void checkRange(int offset, int quantity) throws IllegalDataAddressException {
        if (offset + quantity > getQuantity())
            throw new IllegalDataAddressException(offset);
    }

    private void checkAddress(int offset) throws IllegalDataAddressException {
        if (DataUtils.wordLow(offset) > (getQuantity() - 1))
            throw new IllegalDataAddressException(offset);
    }

    private void checkValue(int value) throws IllegalDataValueException {
        if (!Modbus.checkRegisterValue(value))
            throw new IllegalDataValueException();
    }
}
