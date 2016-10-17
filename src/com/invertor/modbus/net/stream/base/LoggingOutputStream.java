package com.invertor.modbus.net.stream.base;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.utils.ByteFifo;
import com.invertor.modbus.utils.DataUtils;

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

public class LoggingOutputStream extends ModbusOutputStream {

    /**
     * The output stream to be logged
     */
    final private ModbusOutputStream out;
    final private ByteFifo fifo = new ByteFifo(Modbus.MAX_PDU_LENGTH);

    public LoggingOutputStream(ModbusOutputStream out) {
        this.out = out;
    }


    @Override
    public void write(byte[] b) throws IOException {
        out.write(b);
        if (Modbus.getLogLevel() == Modbus.LogLevel.LEVEL_DEBUG)
            fifo.write(b);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        if (Modbus.getLogLevel() == Modbus.LogLevel.LEVEL_DEBUG)
            fifo.write(b);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
        log();
    }

    public void log() {
        if (Modbus.getLogLevel() == Modbus.LogLevel.LEVEL_DEBUG) {
            Modbus.log().info("Frame sent: " + DataUtils.toAscii(fifo.toByteArray()));
            fifo.reset();
        }
    }

    public byte[] toByteArray() {
        return out.toByteArray();
    }
}
