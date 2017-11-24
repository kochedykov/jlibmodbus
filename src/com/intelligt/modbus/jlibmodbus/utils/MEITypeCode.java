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
public enum MEITypeCode {
    CAN_OPEN_PDU(0x0D),
    READ_DEVICE_IDENTIFICATION(0x0E),
    RESERVED(0xff);

    final private int value;

    MEITypeCode(int value) {
        this.value = value;
    }

    static public MEITypeCode get(int value) {
        for (MEITypeCode func : MEITypeCode.values()) {
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