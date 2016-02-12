package com.invertor.examples.modbus;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.ModbusSlave;
import com.invertor.modbus.ModbusSlaveFactory;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.data.SimpleDataHolderBuilder;
import com.invertor.modbus.exception.IllegalDataAddressException;
import com.invertor.modbus.exception.IllegalDataValueException;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.serial.SerialPort;
import com.invertor.modbus.serial.SerialUtils;

import java.net.InetAddress;

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
public class ModbusSlaveTest {

    static private <T> T initParameter(String title, T parameter, String arg, ParameterInitializer<T> pi) {
        try {
            parameter = pi.init(arg);
        } catch (Exception e) {
            System.out.format("Invalid %s value:%s\n", title, arg);
        }
        return parameter;
    }

    public static void main(String[] argv) {
        if (argv.length < 1) {
            printUsage();
            return;
        }

        ModbusSlave slave;

        switch (TransportType.get(argv[0])) {

            case TCP:
                String host = "localhost";
                int port = Modbus.TCP_PORT;
                try {
                    host = initParameter("host", host, argv[1], new ParameterInitializer<String>() {
                        @Override
                        public String init(String arg) throws Exception {
                            return InetAddress.getByName(arg).getHostAddress();
                        }
                    });
                    port = initParameter("port", port, argv[2], new ParameterInitializer<Integer>() {
                        @Override
                        public Integer init(String arg) throws Exception {
                            return Integer.decode(arg);
                        }
                    });
                } catch (IndexOutOfBoundsException ie) {
                    //it's ok
                }
                System.out.format("Starting Modbus Slave TCP with settings:\n\t%s\n", host);
                slave = ModbusSlaveFactory.createModbusSlaveTCP(host, port);
                break;
            case RTU:
                String device_name = SerialUtils.getPortList()[0];
                SerialPort.BaudRate baud_rate = SerialPort.BaudRate.BAUD_RATE_115200;
                int data_bits = 8;
                int stop_bits = 1;
                SerialPort.Parity parity = SerialPort.Parity.NONE;

                try {
                    device_name = initParameter("device_name", device_name, argv[1], new ParameterInitializer<String>() {
                        @Override
                        public String init(String arg) throws Exception {
                            return arg;
                        }
                    });
                    baud_rate = initParameter("baud_rate", baud_rate, argv[2], new ParameterInitializer<SerialPort.BaudRate>() {
                        @Override
                        public SerialPort.BaudRate init(String arg) throws Exception {
                            return SerialPort.BaudRate.getBaudRate(Integer.decode(arg));
                        }
                    });
                    data_bits = initParameter("data_bits", data_bits, argv[3], new ParameterInitializer<Integer>() {
                        @Override
                        public Integer init(String arg) throws Exception {
                            return Integer.decode(arg);
                        }
                    });
                    stop_bits = initParameter("stop_bits", data_bits, argv[4], new ParameterInitializer<Integer>() {
                        @Override
                        public Integer init(String arg) throws Exception {
                            return Integer.decode(arg);
                        }
                    });
                    parity = initParameter("parity", parity, argv[5], new ParameterInitializer<SerialPort.Parity>() {
                        @Override
                        public SerialPort.Parity init(String arg) throws Exception {
                            return SerialPort.Parity.getParity(Integer.decode(arg));
                        }
                    });
                } catch (IndexOutOfBoundsException ie) {
                    //it's ok
                }
                System.out.format("Starting ModbusMaster RTU with settings:\n\t%s, %s, %d, %d, %s\n",
                        device_name, baud_rate.toString(), data_bits, stop_bits, parity.toString());
                slave = ModbusSlaveFactory.createModbusSlaveRTU(device_name, baud_rate, data_bits, stop_bits, parity);
                break;
            case ASCII:
                device_name = SerialUtils.getPortList()[0];
                baud_rate = SerialPort.BaudRate.BAUD_RATE_115200;
                parity = SerialPort.Parity.ODD;
                try {
                    device_name = initParameter("device_name", device_name, argv[1], new ParameterInitializer<String>() {
                        @Override
                        public String init(String arg) throws Exception {
                            return arg;
                        }
                    });
                    baud_rate = initParameter("baud_rate", baud_rate, argv[2], new ParameterInitializer<SerialPort.BaudRate>() {
                        @Override
                        public SerialPort.BaudRate init(String arg) throws Exception {
                            return SerialPort.BaudRate.getBaudRate(Integer.decode(arg));
                        }
                    });
                    parity = initParameter("parity", parity, argv[3], new ParameterInitializer<SerialPort.Parity>() {
                        @Override
                        public SerialPort.Parity init(String arg) throws Exception {
                            return SerialPort.Parity.getParity(Integer.decode(arg));
                        }
                    });
                } catch (IndexOutOfBoundsException ie) {
                    //it's ok
                }
                System.out.format("Starting ModbusMaster ASCII with settings:\n\t%s, %s, %s\n",
                        device_name, baud_rate.toString(), parity.toString());
                slave = ModbusSlaveFactory.createModbusSlaveASCII(device_name, baud_rate, parity);
                break;
            default:
                slave = ModbusSlaveFactory.createModbusSlaveTCP("localhost");
        }

        slave.setServerAddress(1);
        slave.setDataHolder(new SimpleDataHolderBuilder(1000));

        try {
            DataHolder dataHolder = slave.getDataHolder();
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

        try {
            slave.open();
            while (true) {
                Thread.sleep(1000);
                System.out.println();
                System.out.println("Slave output");
                printRegisters("Holding registers", slave.getDataHolder().getHoldingRegisters().getRange(0, 10));
                printRegisters("Input registers", slave.getDataHolder().getInputRegisters().getRange(0, 10));
                printBits("Coils", slave.getDataHolder().getCoils().getRange(0, 10));
                printBits("Discrete inputs", slave.getDataHolder().getDiscreteInputs().getRange(0, 10));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalDataAddressException e) {
            e.printStackTrace();
        } catch (IllegalDataValueException e) {
            e.printStackTrace();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        } finally {
            try {
                slave.close();
            } catch (ModbusIOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printRegisters(String title, int[] ir) {
        for (int i : ir)
            System.out.format("%6d", i);
        System.out.format("\t%s\n", title);
    }

    private static void printBits(String title, boolean[] ir) {
        for (boolean i : ir)
            System.out.format("%6s", i);
        System.out.format("\t%s\n", title);
    }

    private static void printUsage() {
        System.out.format("Usage:%s [%s, %s, %s]\n", ModbusSlaveTest.class.getCanonicalName(), "tcp", "rtu", "ascii");
        System.out.println("Additional parameters:");
        System.out.format("\t%s:\t%s %s\n\t\t%s\n", "TCP",
                "host", "port",
                "Example: 127.0.0.1 502");
        System.out.format("\t%s:\t%s %s %s %s %s\n\t\t%s\n", "RTU",
                "device_name", "baud_rate", "data_bits", "stop_bits", "parity(none, odd, even, mark, space)",
                "Example: COM1 115200 8 1 none");
        System.out.format("\t%s:\t%s %s %s\n\t\t%s\n", "ASCII",
                "device_name", "baud_rate", "parity(none, odd, even, mark, space)",
                "Example: COM1 115200 odd");
    }

    private enum TransportType {
        TCP("tcp"),
        RTU("rtu"),
        ASCII("ascii");

        final private String type;

        TransportType(String type) {
            this.type = type;
        }

        static public TransportType get(String s) {
            for (TransportType type : TransportType.values()) {
                if (type.toString().equalsIgnoreCase(s))
                    return type;
            }
            return TCP;
        }

        @Override
        final public String toString() {
            return type;
        }
    }

    interface ParameterInitializer<T> {
        T init(String arg) throws Exception;
    }
}
