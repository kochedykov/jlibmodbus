package com.invertor.modbus.exception;

import com.invertor.modbus.utils.ModbusExceptionCode;

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

/**
 * quote from MODBUS Application Protocol Specification V1.1b
 * <p/>
 * "Specialized use in conjunction with function codes
 * 20 and 21 and reference type 6, to indicate that
 * the extended file area failed to pass a consistency
 * check.
 * The server (or slave) attempted to read record
 * file, but detected a parity error in the memory.
 * The client (or master) can retry the request, but
 * service may be required on the server (or slave)
 * device."
 */
public class MemoryParityErrorException extends ModbusProtocolException {
    public MemoryParityErrorException() {
        super(ModbusExceptionCode.MEMORY_PARITY_ERROR);
    }
}
