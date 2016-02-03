package com.sbpinvertor.tests.modbus;

import com.sbpinvertor.modbus.ModbusMaster;
import com.sbpinvertor.modbus.ModbusMasterFactory;

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
public class ModbusMasterTest {

    public static void main(String[] argv) throws IOException {
        //ModbusMaster m = ModbusMasterFactory.createModbusMasterRTU("COM1", SerialPort.BaudRate.BAUD_RATE_115200, 8, 1, SerialPort.Parity.NONE);
        //ModbusMaster m = ModbusMasterFactory.createModbusMasterASCII("COM1", SerialPort.BaudRate.BAUD_RATE_115200);
        ModbusMaster m = ModbusMasterFactory.createModbusMasterTCP("127.0.0.1", false);
        m.setResponseTimeout(1000);
        m.open();

        for (int r = 0; r < 5; r++) {
            try {
                Thread.sleep(1000);
                printRegisters(m.readHoldingRegisters(1, 0, 10));
                printRegisters(m.readInputRegisters(1, 0, 10));
                printBits(m.readCoils(1, 0, 8));
                printBits(m.readDiscreteInputs(1, 0, 8));
                m.writeSingleRegister(1, 0, 69);
                m.writeSingleCoil(1, 5, true);
                m.writeMultipleRegisters(1, 1, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
                m.writeMultipleCoils(1, 0, new boolean[]{true, false, true});
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        m.close();
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
