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
