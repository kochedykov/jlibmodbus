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

    static public ModbusExceptionCode get(int value) {
        for (ModbusExceptionCode type : ModbusExceptionCode.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return UNKNOWN_EXCEPTION;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name() + ": Exception Code = " + value;
    }
}