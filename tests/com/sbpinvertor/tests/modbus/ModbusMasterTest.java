package com.sbpinvertor.tests.modbus;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.ModbusMaster;

/**
 * Copyright (c) 2015 JSC "Zavod "Invertor"
 * [http://www.sbp-invertor.ru]
 * <p/>
 * This file is part of JLibModbus.
 * <p/>
 * JLibModbus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ModbusMasterTest {

    public static void main(String[] argv) {
        //Modbus.createModbusMasterRTU("COM1", SerialPort.BaudRate.BAUD_RATE_115200, 8, 1, SerialPort.Parity.NONE);
        ModbusMaster m = Modbus.createModbusMasterTCP("127.0.0.1", 502, true);
        for (;;) {
            try {
                int[] arr = m.readHoldingRegisters(7, 1162, 1);
                for (int i : arr)
                    System.out.print(i + " ");
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
