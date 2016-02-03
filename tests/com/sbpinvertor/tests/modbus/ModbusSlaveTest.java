package com.sbpinvertor.tests.modbus;

import com.sbpinvertor.modbus.ModbusSlave;
import com.sbpinvertor.modbus.ModbusSlaveFactory;
import com.sbpinvertor.modbus.serial.SerialPort;

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
public class ModbusSlaveTest {

    public static void main(String[] argv) throws IOException {
        ModbusSlave s = ModbusSlaveFactory.createModbusSlaveRTU("COM1", SerialPort.BaudRate.BAUD_RATE_115200, 8, 1, SerialPort.Parity.NONE);
        //ModbusSlave m = ModbusSlaveFactory.createModbusSlaveASCII("COM1", SerialPort.BaudRate.BAUD_RATE_115200);
        //ModbusSlave m = ModbusSlaveFactory.createModbusSlaveTCP("127.0.0.1");
        try {
            s.open();
            for (; ; ) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }

    }

    private static void printRegisters(int[] ir) {
        for (int i : ir)
            System.out.print(i);
        System.out.println();
    }

    private static void printBits(boolean[] ir) {
        for (boolean i : ir)
            System.out.print(i + " ");
        System.out.println();
    }
}
