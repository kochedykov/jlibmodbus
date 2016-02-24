package com.invertor.modbus.utils;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

/*
the list of available modbus functions

01 (0x01) Read Coils
02 (0x02) Read Discrete Inputs
03 (0x03) Read Holding Registers
04 (0x04) Read Input Registers
05 (0x05) Write Single Coil
06 (0x06) Write Single Register
07 (0x07) Read Exception Status (Serial Line only)
08 (0x08) Diagnostics (Serial Line only)
11 (0x0B) Get Comm Event Counter (Serial Line only)
12 (0x0C) Get Comm Event Log (Serial Line only)
15 (0x0F) Write Multiple Coils
16 (0x10) Write Multiple registers
17 (0x11) Report Slave ID (Serial Line only)
20 (0x14) Read File Record
21 (0x15) Write File Record
22 (0x16) Mask Write Register
23 (0x17) Read/Write Multiple registers
24 (0x18) Read FIFO Queue
43 ( 0x2B) Encapsulated Interface Transport
43 / 13 (0x2B / 0x0D) CAN open General Reference Request and Response PDU
43 / 14 (0x2B / 0x0E) Read Device Identification
*/

public enum ModbusFunctionCode {
    READ_COILS(0x1),
    READ_DISCRETE_INPUTS(0x2),
    READ_HOLDING_REGISTERS(0x3),
    READ_INPUT_REGISTERS(0x4),
    WRITE_SINGLE_COIL(0x5),
    WRITE_SINGLE_REGISTER(0x6),
    READ_EXCEPTION_STATUS(0x7),//serial line only
    DIAGNOSTICS(0x8),//serial line only
    GET_COMM_EVENT_COUNTER(0x0B),//serial line only
    GET_COMM_EVENT_LOG(0x0C),//serial line only
    WRITE_MULTIPLE_COILS(0x0F),
    WRITE_MULTIPLE_REGISTERS(0x10),
    REPORT_SLAVE_ID(0x11),//serial line only
    READ_FILE_RECORD(0x14),
    WRITE_FILE_RECORD(0x15),
    MASK_WRITE_REGISTER(0x16),
    READ_WRITE_MULTIPLE_REGISTERS(0x17),
    READ_FIFO_QUEUE(0x18),
    ENCAPSULATED_INTERFACE_TRANSPORT(0x2B),//implemented Read Device Identification interface, (0x2B / 0x0E)
    UNKNOWN(0xff);

    final static private int MODBUS_EXCEPTION_FLAG = 0x80;
    final private int value;

    ModbusFunctionCode(int value) {
        this.value = value;
    }

    static public ModbusFunctionCode get(int value) {
        if (isException(value)) {
            value &= ~MODBUS_EXCEPTION_FLAG;
        }
        for (ModbusFunctionCode func : ModbusFunctionCode.values()) {
            if (func.value == value) {
                return func;
            }
        }
        return UNKNOWN;
    }

    static public boolean isException(int value) {
        return (value & MODBUS_EXCEPTION_FLAG) != 0;
    }

    static public int getExceptionValue(int functionCode) {
        return functionCode | MODBUS_EXCEPTION_FLAG;
    }

    public int toInt() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}