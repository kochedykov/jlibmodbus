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
