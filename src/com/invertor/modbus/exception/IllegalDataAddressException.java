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
 * "The data address received in the query is not an
 * allowable address for the server (or slave). More
 * specifically, the combination of reference number
 * and transfer length is invalid. For a controller with
 * 100 registers, the PDU addresses the first
 * register as 0, and the last one as 99. If a request
 * is submitted with a starting register address of 96
 * and a quantity of registers of 4, then this request
 * will successfully operate (address-wise at least)
 * on registers 96, 97, 98, 99. If a request is
 * submitted with a starting register address of 96
 * and a quantity of registers of 5, then this request
 * will fail with Exception Code 0x02 “Illegal Data
 * Address” since it attempts to operate on registers
 * 96, 97, 98, 99 and 100, and there is no register
 * with address 100."
 */
public class IllegalDataAddressException extends ModbusProtocolException {
    final private int dataAddress;

    public IllegalDataAddressException(int dataAddress) {
        super(ModbusExceptionCode.ILLEGAL_DATA_ADDRESS);
        this.dataAddress = dataAddress;
    }

    public int getDataAddress() {
        return dataAddress;
    }
}
