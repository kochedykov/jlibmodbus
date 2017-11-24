package com.intelligt.modbus.jlibmodbus.msg.base;

import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException;

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
public class ModbusFileRecord {
    final static public int REF_TYPE = 0x06;
    final private int number;
    final private int file;
    private int length = 0;
    private int[] registers;

    public ModbusFileRecord(int fileNumber, int recordNumber, int registerCount) {
        this.file = fileNumber;
        this.number = recordNumber;
        this.length = registerCount;
    }

    public ModbusFileRecord(int file, int number, int[] buffer) {
        this.file = file;
        this.number = number;
        setRegisters(buffer);
    }

    public int[] getRegisters() {
        return Arrays.copyOf(registers, registers.length);
    }

    /**
     * setter
     *
     * @param registers modbus register values
     */
    protected void setRegisters(int[] registers) {
        this.registers = Arrays.copyOf(registers, registers.length);
        this.length = registers.length;
    }

    public void writeRegisters(int[] registers) throws IllegalDataValueException {
        if (registers.length > getRegisters().length)
            throw new IllegalDataValueException();
        this.length = registers.length;
    }

    public int getFileNumber() {
        return file;
    }

    public int getRecordNumber() {
        return number;
    }

    public int getRecordLength() {
        return length;
    }
}
