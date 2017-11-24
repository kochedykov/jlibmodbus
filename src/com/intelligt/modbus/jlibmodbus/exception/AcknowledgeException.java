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
 * "Specialized use in conjunction with programming
 * commands.
 * The server (or slave) has accepted the request
 * and is processing it, but a long duration of time
 * will be required to do so. This response is
 * returned to prevent a timeout error from occurring
 * in the client (or master). The client (or master)
 * can next issue a Poll Program Complete message
 * to determine if processing is completed."
 */
public class AcknowledgeException extends ModbusProtocolException {
    public AcknowledgeException() {
        super(ModbusExceptionCode.ACKNOWLEDGE);
    }
}
