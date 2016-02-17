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

/**
 *  The list of sub-function codes supported by the serial line devices.
 * Hex  Dec
 * 00   00 Return Query Data
 * 01   01 Restart Communications Option
 * 02   02 Return Diagnostic Register
 * 03   03 Change ASCII Input Delimiter
 * 04   04 Force Listen Only Mode
 * 05.. 09 RESERVED
 * 0A   10 Clear Counters and Diagnostic Register
 * 0B   11 Return Bus Message Count
 * 0C   12 Return Bus Communication Error Count
 * 0D   13 Return Bus Exception Error Count
 * 0E   14 Return Slave Message Count
 * 0F   15 Return Slave No Response Count
 * 10   16 Return Slave NAK Count
 * 11   17 Return Slave Busy Count
 * 12   18 Return Bus Character Overrun Count
 * 13   19 RESERVED
 * 14   20 Clear Overrun Counter and Flag
 * N.A. 21 ...
 * 65535
 */
public enum DiagnosticsSubFunctionCode {
    RETURN_QUERY_DATA(0x0),
    RESTART_COMMUNICATIONS_OPTION(0x1),
    RETURN_DIAGNOSTIC_REGISTER(0x2),
    CHANGE_ASCII_INPUT_DELIMITER(0x3),
    FORCE_LISTEN_ONLY_MODE(0x4),
    CLEAR_COUNTERS_AND_DIAGNOSTIC_REGISTER(0xA),
    RETURN_BUS_MESSAGE_COUNT(0xB),
    RETURN_BUS_COMMUNICATION_ERROR_COUNT(0xC),
    RETURN_BUS_EXCEPTION_ERROR_COUNT(0x0D),
    RETURN_SLAVE_MESSAGE_COUNT(0x0E),
    RETURN_SLAVE_NO_RESPONSE_COUNT(0x0F),
    RETURN_SLAVE_NAK_COUNT(0x10),
    RETURN_SLAVE_BUSY_COUNT(0x11),
    RETURN_BUS_CHARACTER_OVERRUN_COUNT(0x12),
    CLEAR_OVERRUN_COUNTER_AND_FLAG(0x14),
    RESERVED(0xffff);

    final private int value;

    DiagnosticsSubFunctionCode(int value) {
        this.value = value;
    }

    static public DiagnosticsSubFunctionCode get(int value) {
        for (DiagnosticsSubFunctionCode func : DiagnosticsSubFunctionCode.values()) {
            if (func.value == value) {
                return func;
            }
        }
        return RESERVED;
    }

    public int toInt() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}