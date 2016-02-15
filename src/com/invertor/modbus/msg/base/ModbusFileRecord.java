package com.invertor.modbus.msg.base;

import com.invertor.modbus.exception.IllegalDataValueException;

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
        return registers;
    }

    /**
     * setter
     *
     * @param registers modbus register values
     */
    protected void setRegisters(int[] registers) {
        this.registers = registers;
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
