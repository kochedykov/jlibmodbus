package com.intelligt.modbus.jlibmodbus.data;

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

import com.intelligt.modbus.jlibmodbus.exception.IllegalDataAddressException;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException;

import java.util.Observable;

/**
 * quote from MODBUS Application Protocol Specification V1.1b
 * <p>
 * "A file is an organization of records. Each file contains 10000 records, addressed 0000 to
 * 9999 decimal or 0X0000 to 0X270F. For example, record 12 is addressed as 12.
 * ...
 * The quantity of registers to be read, combined with all other fields in the expected response,
 * must not exceed the allowable length of the MODBUS PDU : 253 bytes."
 * <p>
 * so the length of file record must not exceed 250 bytes(253 - function_code - resp_data_len - sub_req_resp_len).
 */
abstract public class ModbusFile extends Observable {
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

    public void write(int recordNumber, int[] buffer) throws IllegalDataAddressException, IllegalDataValueException {
        notifyObservers(new int[]{recordNumber, buffer.length});
    }

    public int getNumber() {
        return number;
    }
}
