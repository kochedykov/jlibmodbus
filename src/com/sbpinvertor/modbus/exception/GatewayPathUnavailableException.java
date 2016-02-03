package com.sbpinvertor.modbus.exception;

import com.sbpinvertor.modbus.utils.ModbusExceptionCode;

/**
 * Copyright (c) 2015-2016 JSC "Zavod "Invertor"
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

/**
 * quote from MODBUS Application Protocol Specification V1.1b
 * <p/>
 * "Specialized use in conjunction with gateways,
 * indicates that the gateway was unable to allocate
 * an internal communication path from the input
 * port to the output port for processing the request.
 * Usually means that the gateway is misconfigured
 * or overloaded."
 */
public class GatewayPathUnavailableException extends ModbusProtocolException {
    public GatewayPathUnavailableException(int serverAddress) {
        super(ModbusExceptionCode.GATEWAY_PATH_UNAVAILABLE, serverAddress);
    }
}
