package com.intelligt.modbus.jlibmodbus.exception;

import com.intelligt.modbus.jlibmodbus.utils.ModbusExceptionCode;

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
 * quote from MODBUS Application Protocol Specification V1.1b
 * <p>
 * "A value contained in the query data field is not an
 * allowable value for server (or slave). This
 * indicates a fault in the structure of the remainder
 * of a complex request, such as that the implied
 * length is incorrect. It specifically does NOT mean
 * that a data item submitted for storage in a register
 * has a value outside the expectation of the
 * application program, since the MODBUS protocol
 * is unaware of the significance of any particular
 * value of any particular register."
 */
public class IllegalDataValueException extends ModbusProtocolException {

    public IllegalDataValueException() {
        super(ModbusExceptionCode.ILLEGAL_DATA_VALUE);
    }
}
