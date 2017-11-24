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
 * "The function code received in the query is not an
 * allowable action for the server (or slave). This
 * may be because the function code is only
 * applicable to newer devices, and was not
 * implemented in the unit selected. It could also
 * indicate that the server (or slave) is in the wrong
 * state to process a request of this type, for
 * example because it is unconfigured and is being
 * asked to return register values."
 */
public class IllegalFunctionException extends ModbusProtocolException {

    final private int functionCode;

    public IllegalFunctionException(int functionCode) {
        super(ModbusExceptionCode.ILLEGAL_FUNCTION);
        this.functionCode = functionCode;
    }

    public int getFunctionCode() {
        return functionCode;
    }
}
