package com.invertor.modbus.data;

import com.invertor.modbus.exception.IllegalDataAddressException;
import com.invertor.modbus.exception.IllegalDataValueException;
import com.invertor.modbus.msg.base.ModbusFileRecord;
import com.invertor.modbus.utils.DataUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class SimpleModbusFile extends ModbusFile {

    final private Map<Integer, ModbusFileRecord> records = new TreeMap<Integer, ModbusFileRecord>();

    public SimpleModbusFile(int number) {
        super(number);
    }

    @Override
    public int[] read(int recordNumber, int recordLength) throws IllegalDataAddressException {
        if (!records.containsKey(recordNumber))
            throw new IllegalDataAddressException(recordNumber);
        int[] buffer = records.get(recordNumber).getRegisters();
        if (buffer.length < recordLength)
            throw new IllegalDataAddressException(recordLength);
        return Arrays.copyOf(buffer, recordLength);
    }

    @Override
    public void write(int recordNumber, int[] buffer) throws IllegalDataAddressException, IllegalDataValueException {
        if (records.containsKey(recordNumber)) {
            records.get(recordNumber).writeRegisters(buffer);
        } else {
            records.put(recordNumber, new ModbusFileRecord(getNumber(), recordNumber, buffer));
        }
    }

    @Override
    void write(int recordNumber, byte[] buffer) throws IllegalDataAddressException, IllegalDataValueException {
        write(recordNumber, DataUtils.toIntArray(buffer));
    }
}
