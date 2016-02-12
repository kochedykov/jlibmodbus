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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public enum ModbusExceptionCode {
    ILLEGAL_FUNCTION(0x01),
    ILLEGAL_DATA_ADDRESS(0x02),
    ILLEGAL_DATA_VALUE(0x03),
    SLAVE_DEVICE_FAILURE(0x04),
    ACKNOWLEDGE(0x05),
    SLAVE_DEVICE_BUSY(0x06),
    MEMORY_PARITY_ERROR(0x08),
    GATEWAY_PATH_UNAVAILABLE(0x0A),
    GATEWAY_TARGET_DEVICE_FAILED_TO_RESPOND(0x0B),

    UNKNOWN_EXCEPTION(0x100),
    NO_EXCEPTION(0x101);

    private final int value;

    ModbusExceptionCode(int value) {
        this.value = value;
    }

    static private ModbusExceptionCode get(int value) {
        for (ModbusExceptionCode type : ModbusExceptionCode.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return UNKNOWN_EXCEPTION;
    }

    static public ModbusExceptionCode getExceptionCode(int value) {
        return get(value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name() + ": Exception Code = " + value;
    }
}