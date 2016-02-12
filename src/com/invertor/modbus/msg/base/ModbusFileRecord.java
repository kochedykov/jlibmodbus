package com.invertor.modbus.msg.base;

import com.invertor.modbus.utils.DataUtils;

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
    final private int file;
    final private int number;
    private byte[] buffer = null;

    public ModbusFileRecord(int file, int number) {
        this.file = file;
        this.number = number;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    /**
     * setter for file record data buffer.
     * this function converts buffer of modbus register values to byte array.
     *
     * @param registers modbus register values
     */
    public void setBuffer(int[] registers) {
        this.buffer = DataUtils.toByteArray(registers);
    }

    /**
     * setter
     *
     * @param buffer array of bytes
     */
    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public int getFileNumber() {
        return file;
    }

    public int getRecordNumber() {
        return number;
    }

    public int getRecordLength() {
        return buffer.length;
    }
}
