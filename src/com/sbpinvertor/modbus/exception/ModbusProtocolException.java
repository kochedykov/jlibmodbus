package com.sbpinvertor.modbus.exception;

import com.sbpinvertor.modbus.data.base.ModbusException;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

public class ModbusProtocolException extends Exception {

    private final ModbusException code;

    public ModbusProtocolException(ModbusException code) {
        super();

        this.code = code;
    }

    public ModbusProtocolException(String message, ModbusException code) {
        super(message);

        this.code = code;
    }

    public ModbusProtocolException(Throwable cause, ModbusException code) {
        super(cause);

        this.code = code;
    }

    public ModbusException getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        return getCode().toString() + ": " + message;
    }
}
