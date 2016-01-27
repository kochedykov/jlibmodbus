package com.sbpinvertor.modbus.tcp;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.msg.base.Transportable;
import com.sbpinvertor.modbus.net.stream.base.ModbusInputStream;
import com.sbpinvertor.modbus.net.stream.base.ModbusOutputStream;
import com.sbpinvertor.modbus.utils.DataUtils;

import java.io.IOException;

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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
final public class TcpAduHeader implements Transportable {
    final static public int SIZE = 6;
    final private byte[] buffer;

    public TcpAduHeader() {
        buffer = new byte[SIZE];
        setProtocolId(Modbus.PROTOCOL_ID);
    }

    private void setBufferValue(int value, int offset) {
        buffer[offset++] = DataUtils.byteHigh(value);
        buffer[offset] = DataUtils.byteLow(value);
    }

    public short getPduSize() {
        return DataUtils.toShort(buffer[4], buffer[5]);
    }

    public void setPduSize(int value) {
        setBufferValue(value, 4);
    }

    public short getProtocolId() {
        return DataUtils.toShort(buffer[2], buffer[3]);
    }

    public void setProtocolId(int value) {
        setBufferValue(value, 2);
    }

    public short getTransactionId() {
        return DataUtils.toShort(buffer[0], buffer[1]);
    }

    public void setTransactionId(int value) {
        setBufferValue(value, 0);
    }

    public byte[] byteArray() {
        return buffer;
    }

    @Override
    public void write(ModbusOutputStream fifo) throws IOException {
        fifo.write(byteArray());
    }

    @Override
    public void read(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        fifo.read(byteArray());
        if (getPduSize() > Modbus.MAX_PDU_LENGTH) {
            throw new ModbusNumberException("Maximum PDU size is reached.", getPduSize());
        }
    }
}