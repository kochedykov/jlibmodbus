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
 * "Specialized use in conjunction with gateways,
 * indicates that the gateway was unable to allocate
 * an internal communication path from the input
 * port to the output port for processing the request.
 * Usually means that the gateway is misconfigured
 * or overloaded."
 */
public class GatewayPathUnavailableException extends ModbusProtocolException {
    public GatewayPathUnavailableException() {
        super(ModbusExceptionCode.GATEWAY_PATH_UNAVAILABLE);
    }
}
