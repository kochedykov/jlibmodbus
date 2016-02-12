package com.invertor.modbus.data;

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

import com.invertor.modbus.exception.IllegalDataAddressException;
import com.invertor.modbus.exception.IllegalDataValueException;

/**
 * quote from MODBUS Application Protocol Specification V1.1b
 * <p/>
 * "A file is an organization of records. Each file contains 10000 records, addressed 0000 to
 * 9999 decimal or 0X0000 to 0X270F. For example, record 12 is addressed as 12.
 * ...
 * The quantity of registers to be read, combined with all other fields in the expected response,
 * must not exceed the allowable length of the MODBUS PDU : 253 bytes."
 * <p/>
 * so the length of file record must not exceed 250 bytes(253 - function_code - resp_data_len - sub_req_resp_len).
 */
abstract public class ModbusFile {
    private final int number;

    public ModbusFile(int number) {
        this.number = number;
    }

    /**
     * read modbus file record
     *
     * @param recordNumber number of a record
     * @param recordLength read register count
     * @return record data
     * @throws IllegalDataAddressException record with number recordNumber not exist or recordLength bytes not allowable.
     */
    abstract public int[] read(int recordNumber, int recordLength) throws IllegalDataAddressException;

    abstract public void write(int recordNumber, int[] buffer) throws IllegalDataAddressException, IllegalDataValueException;

    public int getNumber() {
        return number;
    }
}
