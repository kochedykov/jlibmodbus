package com.intelligt.modbus.jlibmodbus.utils;

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
 * The list of sub-function codes supported by the serial line devices.
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