package com.sbpinvertor.modbus.net;

import com.sbpinvertor.conn.SerialPort;
import com.sbpinvertor.conn.SerialPortException;
import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.data.ModbusInputStream;
import com.sbpinvertor.modbus.data.ModbusOutputStream;
import com.sbpinvertor.modbus.data.base.ModbusMessage;
import com.sbpinvertor.modbus.data.base.ModbusResponse;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.utils.ByteFifo;
import com.sbpinvertor.utils.CRC16;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ModbusTransportRTU extends ModbusTransport {
    final private SerialPortOutputStream os;
    final private SerialPortInputStream is;

    public ModbusTransportRTU(SerialPort port) throws SerialPortException {
        os = new SerialPortOutputStream(port);
        is = new SerialPortInputStream(port);
        port.open();
    }

    public ModbusOutputStream getOutputStream() {
        return os;
    }

    public ModbusInputStream getInputStream() {
        return is;
    }

    @Override
    public ModbusResponse sendRequest(ModbusMessage msg) throws ModbusTransportException {
        ModbusResponse response;
        try {
            os.clear();
            is.clear();
            response = super.sendRequest(msg);
            is.read();
            is.read();
        } catch (Exception e) {
            throw new ModbusTransportException(e);
        }
        if (is.getCrc() != 0)
            throw new ModbusTransportException("CRC check failed");
        return response;
    }

    private class SerialPortInputStream extends ModbusInputStream {

        final private SerialPort serial;

        private int crc;

        protected SerialPortInputStream(SerialPort serial) {
            this.serial = serial;
            crc = CRC16.INITIAL_VALUE;
        }

        public void clear() throws IOException {
            serial.clear();
            crc = CRC16.INITIAL_VALUE;
        }

        @Override
        public int read() throws IOException {
            int b = 0xFF & serial.read();
            crc = CRC16.calc(crc, (byte) (b & 0xFF));
            return b;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int c = serial.read(b, off, len);
            crc = CRC16.calc(crc, b, off, len);
            return c;
        }

        public int getCrc() {
            return crc;
        }
    }

    private class SerialPortOutputStream extends ModbusOutputStream {

        final private SerialPort serial;
        private final ByteFifo fifo = new ByteFifo(Modbus.MAX_RTU_ADU_LENGTH);
        private int crc;

        protected SerialPortOutputStream(SerialPort serial) {
            this.serial = serial;
            crc = CRC16.INITIAL_VALUE;
        }

        @Override
        public void write(byte[] b) throws IOException {
            fifo.write(b);
            crc = CRC16.calc(crc, b, 0, b.length);
        }

        @Override
        public void write(int b) throws IOException {
            fifo.write(b);
            crc = CRC16.calc(crc, (byte) (b & 0xFF));
        }

        @Override
        public void flush() throws IOException {
            fifo.writeShortLE(crc);
            serial.write(fifo.toByteArray());
            clear();
        }

        public void clear() throws IOException {
            fifo.clear();
            crc = CRC16.INITIAL_VALUE;
        }
    }
}
