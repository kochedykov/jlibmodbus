package com.invertor.modbus.msg.base.mei;

import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.IllegalDataAddressException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.utils.MEITypeCode;

import java.io.IOException;

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
public interface ModbusEncapsulatedInterface {
    MEITypeCode getTypeCode();

    void writeRequest(ModbusOutputStream fifo) throws IOException;

    void readRequest(ModbusInputStream fifo) throws IOException;

    int getRequestSize();

    void writeResponse(ModbusOutputStream fifo) throws IOException;

    void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException;

    int getResponseSize();

    void process(DataHolder dataHolder) throws IllegalDataAddressException;
}
