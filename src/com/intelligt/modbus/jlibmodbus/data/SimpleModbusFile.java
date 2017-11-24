package com.intelligt.modbus.jlibmodbus.data;

import com.intelligt.modbus.jlibmodbus.exception.IllegalDataAddressException;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusFileRecord;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

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
public class SimpleModbusFile extends ModbusFile {

    final private Map<Integer, ModbusFileRecord> records = new TreeMap<Integer, ModbusFileRecord>();

    public SimpleModbusFile(int number) {
        super(number);
    }

    @Override
    synchronized public int[] read(int recordNumber, int recordLength) throws IllegalDataAddressException {
        if (!records.containsKey(recordNumber))
            throw new IllegalDataAddressException(recordNumber);
        int[] buffer = records.get(recordNumber).getRegisters();
        if (buffer.length < recordLength)
            throw new IllegalDataAddressException(recordLength);
        return Arrays.copyOf(buffer, recordLength);
    }

    @Override
    synchronized public void write(int recordNumber, int[] buffer) throws IllegalDataAddressException, IllegalDataValueException {
        if (records.containsKey(recordNumber)) {
            records.get(recordNumber).writeRegisters(buffer);
        } else {
            records.put(recordNumber, new ModbusFileRecord(getNumber(), recordNumber, buffer));
        }
        super.write(recordNumber, buffer);
    }
}
