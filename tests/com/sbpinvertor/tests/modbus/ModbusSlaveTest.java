package com.sbpinvertor.tests.modbus;

import com.sbpinvertor.modbus.ModbusSlave;
import com.sbpinvertor.modbus.ModbusSlaveFactory;
import com.sbpinvertor.modbus.data.DataHolder;
import com.sbpinvertor.modbus.data.SimpleCoils;
import com.sbpinvertor.modbus.data.SimpleHoldingRegisters;
import com.sbpinvertor.modbus.exception.IllegalDataAddressException;
import com.sbpinvertor.modbus.exception.IllegalDataValueException;
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
        ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveRTU("COM1", SerialPort.BaudRate.BAUD_RATE_115200, 8, 1, SerialPort.Parity.NONE);
        //ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveASCII("COM1", SerialPort.BaudRate.BAUD_RATE_115200);
        //ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveTCP("127.0.0.1");

        slave.setServerAddress(1);
        DataHolder dataHolder = new DataHolder();//or slave.getDataHolder();
        dataHolder.setInputRegisters(new SimpleHoldingRegisters(1000));
        dataHolder.setHoldingRegisters(new SimpleHoldingRegisters(1000));
        dataHolder.setDiscreteInputs(new SimpleCoils(1000));
        dataHolder.setCoils(new SimpleCoils(1000));

        try {
            dataHolder.getCoils().set(1, true);
            dataHolder.getCoils().set(3, true);
            dataHolder.getDiscreteInputs().setRange(0, new boolean[]{false, true, true, false, true});
            dataHolder.getInputRegisters().set(5, 69);
            dataHolder.getHoldingRegisters().setRange(0, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 55});
        } catch (IllegalDataAddressException e) {
            e.printStackTrace();
        } catch (IllegalDataValueException e) {
            e.printStackTrace();
        }

        slave.setDataHolder(dataHolder);//if new DataHolder instance has been created.
        try {
            slave.open();
            while (true) {
                Thread.sleep(1000);
                printRegisters(slave.getDataHolder().getHoldingRegisters().getRange(0, 10));
                printRegisters(slave.getDataHolder().getInputRegisters().getRange(0, 10));
                printBits(slave.getDataHolder().getCoils().getRange(0, 10));
                printBits(slave.getDataHolder().getDiscreteInputs().getRange(0, 10));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalDataAddressException e) {
            e.printStackTrace();
        } catch (IllegalDataValueException e) {
            e.printStackTrace();
        } finally {
            slave.close();
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
