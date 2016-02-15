package com.invertor.modbus.exception;

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
public class ModbusChecksumException extends ModbusIOException {

    public static final String CHECKSUM_ERROR_STRING = "checksum error: received %d, calculated %d";

    public ModbusChecksumException(int recv, int calc) {
        super(String.format(CHECKSUM_ERROR_STRING, recv, calc));
    }

    public ModbusChecksumException(int recv, int calc, Throwable cause) {
        super(String.format(CHECKSUM_ERROR_STRING, recv, calc), cause);
    }
}
