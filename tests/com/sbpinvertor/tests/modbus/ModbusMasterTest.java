package com.sbpinvertor.tests.modbus;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.ModbusMaster;

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
public class ModbusMasterTest {

    public static void main(String[] argv) {
        //ModbusMaster m = Modbus.createModbusMasterRTU("COM1", SerialPort.BaudRate.BAUD_RATE_115200, 8, 1, SerialPort.Parity.NONE);
        //ModbusMaster m = Modbus.createModbusMasterASCII("COM1", SerialPort.BaudRate.BAUD_RATE_115200);
        ModbusMaster m = Modbus.createModbusMasterTCP("127.0.0.1", true);

        for (int r = 0; r < 5; r++) {
            try {
                Thread.sleep(1000);
                printRegisters(m.readHoldingRegisters(0, 0, 2));
                printRegisters(m.readInputRegisters(0, 0, 10));
                m.writeSingleRegister(0, 0, 69);
                m.writeSingleCoil(0, 5, true);
                m.writeMultipleRegisters(0, 0, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
                m.writeMultipleCoils(0, 0, new boolean[]{true, false, true});
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void printRegisters(int[] ir) {
        for (int i : ir)
            System.out.print(i);
        System.out.println();
    }
}
