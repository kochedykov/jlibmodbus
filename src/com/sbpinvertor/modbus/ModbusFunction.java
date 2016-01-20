package com.sbpinvertor.modbus;

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

public enum ModbusFunction {
    READ_COILS((byte) 0x1),
    READ_DISCRETE_INPUTS((byte) 0x2),
    READ_HOLDING_REGISTERS((byte) 0x3),
    READ_INPUT_REGISTERS((byte) 0x4),
    WRITE_SINGLE_COIL((byte) 0x5),
    WRITE_SINGLE_REGISTER((byte) 0x6),
    WRITE_MULTIPLE_COILS((byte) 0x0F),
    WRITE_MULTIPLE_REGISTERS((byte) 0x10),
    REPORT_SLAVE_ID((byte) 0x11),
    READ_FILE_RECORD((byte) 0x14),
    WRITE_FILE_RECORD((byte) 0x15);

    static private final int MODBUS_EXCEPTION_FLAG = 0x80;
    private final int value;

    ModbusFunction(int value) {
        this.value = value;
    }

    static public ModbusFunction getFunction(int value) {
        if (isException(value)) {
            value &= ~MODBUS_EXCEPTION_FLAG;
        }
        for (ModbusFunction func : ModbusFunction.values()) {
            if (func.value == value) {

                return func;
            }
        }
        throw new RuntimeException("Invalid modbus function");
    }

    static public boolean isException(int code) {
        return (code & MODBUS_EXCEPTION_FLAG) != 0;
    }

    public int getCode() {
        return value;
    }

    public int getExceptionCode() {
        return value | MODBUS_EXCEPTION_FLAG;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}