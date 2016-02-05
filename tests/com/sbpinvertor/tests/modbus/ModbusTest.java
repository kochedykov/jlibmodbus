package com.sbpinvertor.tests.modbus;

import com.sbpinvertor.modbus.*;
import com.sbpinvertor.modbus.data.DataHolder;
import com.sbpinvertor.modbus.data.SimpleCoils;
import com.sbpinvertor.modbus.data.SimpleHoldingRegisters;
import com.sbpinvertor.modbus.exception.*;

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
public class ModbusTest implements Runnable {

    private ModbusMaster master;
    private ModbusSlave slave;
    private long timeout = 0;

    public static void main(String[] argv) throws IOException {
        Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);

        ModbusTest test = new ModbusTest();
        //ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveRTU("COM10", SerialPort.BaudRate.BAUD_RATE_115200, 8, 1, SerialPort.Parity.NONE);
        //ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveASCII("COM10", SerialPort.BaudRate.BAUD_RATE_115200);
        test.slave = ModbusSlaveFactory.createModbusSlaveTCP("127.0.0.1");
        //ModbusMaster m = ModbusMasterFactory.createModbusMasterRTU("COM1", SerialPort.BaudRate.BAUD_RATE_115200, 8, 1, SerialPort.Parity.NONE);
        //ModbusMaster m = ModbusMasterFactory.createModbusMasterASCII("COM1", SerialPort.BaudRate.BAUD_RATE_115200);
        test.master = ModbusMasterFactory.createModbusMasterTCP("127.0.0.1", false);
        test.master.setResponseTimeout(1000);

        test.slave.setServerAddress(1);
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
            test.slave.setDataHolder(dataHolder);//if new DataHolder instance has been created.
        } catch (IllegalDataAddressException e) {
            e.printStackTrace();
        } catch (IllegalDataValueException e) {
            e.printStackTrace();
        }

        test.start(10000);
    }

    private static void printRegisters(String title, int[] ir) {
        for (int i : ir)
            System.out.format("%6d", i);
        System.out.format("  %s\n", title);
    }

    private static void printBits(String title, boolean[] ir) {
        for (boolean i : ir)
            System.out.format("%6s", i);
        System.out.format("  %s\n", title);
    }

    private void start(long timeout) {
        this.timeout = timeout;
        Thread thread = new Thread(this);
        thread.start();
        try {
            thread.join(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        try {
            slave.open();
            master.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while ((System.currentTimeMillis() - time) < timeout) {
            try {
                Thread.sleep(1000);
                System.out.println();
                System.out.println("Master output");
                printRegisters("Holding registers", slave.getDataHolder().getHoldingRegisters().getRange(0, 16));
                printRegisters("Input registers", slave.getDataHolder().getInputRegisters().getRange(0, 16));
                printBits("Coils", slave.getDataHolder().getCoils().getRange(0, 16));
                printBits("Discrete inputs", slave.getDataHolder().getDiscreteInputs().getRange(0, 16));
                System.out.println();
                System.out.println("Slave output");
                printRegisters("Holding registers", master.readHoldingRegisters(1, 0, 16));
                printRegisters("Input registers", master.readInputRegisters(1, 0, 16));
                printBits("Coils", master.readCoils(1, 0, 16));
                printBits("Discrete inputs", master.readDiscreteInputs(1, 0, 16));
                master.writeSingleRegister(1, 0, 69);
                master.writeSingleCoil(1, 13, true);
                master.writeMultipleRegisters(1, 1, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
                master.writeMultipleCoils(1, 0, new boolean[]{true, true, true});
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalDataAddressException e) {
                e.printStackTrace();
            } catch (IllegalDataValueException e) {
                e.printStackTrace();
            } catch (ModbusProtocolException e) {
                e.printStackTrace();
            } catch (ModbusMasterException e) {
                e.printStackTrace();
            } catch (ModbusNumberException e) {
                e.printStackTrace();
            } catch (ModbusIOException e) {
                e.printStackTrace();
            }
        }
        try {
            slave.close();
            master.close();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        }
    }
}
