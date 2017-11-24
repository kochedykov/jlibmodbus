package com.intelligt.modbus.jlibmodbus.tcp;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;

import java.io.IOException;

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
final public class TcpAduHeader {
    final private byte[] buffer;

    public TcpAduHeader() {
        /*
        * 2 bytes for transaction id (often not used)
        * 2 bytes for protocol id (should be set to 0)
        * 2 bytes for pdu length (from 3 to 260)
        */
        buffer = new byte[6];
        setProtocolId(Modbus.PROTOCOL_ID);
    }

    private void setBufferValue(int value, int offset) {
        buffer[offset++] = DataUtils.byteHigh(value);
        buffer[offset] = DataUtils.byteLow(value);
    }

    private int getPduSize() {
        return DataUtils.toShort(buffer[4], buffer[5]);
    }

    public void setPduSize(int value) {
        setBufferValue(value, 4);
    }

    public int getProtocolId() {
        return DataUtils.toShort(buffer[2], buffer[3]);
    }

    public void setProtocolId(int value) {
        setBufferValue(value, 2);
    }

    public int getTransactionId() {
        return DataUtils.toShort(buffer[0], buffer[1]);
    }

    public void setTransactionId(int value) {
        setBufferValue(value, 0);
    }

    private byte[] byteArray() {
        return buffer;
    }

    public void write(ModbusOutputStream fifo) throws ModbusIOException {
        try {
            fifo.write(byteArray());
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
    }

    public void read(ModbusInputStream fifo) throws ModbusNumberException, ModbusIOException {
        int size;

        try {
            if ((size = fifo.read(byteArray())) > 0) {

                if (getPduSize() < Modbus.MIN_PDU_LENGTH) {
                    throw new ModbusNumberException("the PDU length is less than the minimum expected.", getPduSize());
                }
                if (getPduSize() > Modbus.MAX_PDU_LENGTH) {
                    throw new ModbusNumberException("Maximum PDU size is reached.", getPduSize());
                }
            } else {
                throw new ModbusIOException(buffer.length + " bytes expected, but " + size + " received.");
            }
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
    }
}