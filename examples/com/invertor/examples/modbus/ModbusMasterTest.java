package com.invertor.examples.modbus;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.ModbusMaster;
import com.invertor.modbus.ModbusMasterFactory;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.serial.SerialPort;
import jssc.SerialPortList;

import java.net.InetAddress;

/*
 * Copyright (C) 2016 "Invertor" Factory", JSC
 * All rights reserved
 *
 * This file is part of JLibModbus.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ModbusMasterTest {

    static private <T> T initParameter(String title, T parameter, String arg, ParameterInitializer<T> pi) {
        try {
            parameter = pi.init(arg);
        } catch (Exception e) {
            System.out.format("Invalid %s value:%s%n", title, arg);
        }
        return parameter;
    }

    public static void main(String[] argv) {
        if (argv.length < 1) {
            printUsage();
            return;
        }

        ModbusMaster master;
        try {
            switch (TransportType.get(argv[0])) {

                case TCP:
                    String host = "localhost";
                    int port = Modbus.TCP_PORT;
                    boolean keepAlive = false;
                    try {
                        host = initParameter("baud_rate", host, argv[1], new ParameterInitializer<String>() {
                            @Override
                            public String init(String arg) throws Exception {
                                return InetAddress.getByName(arg).getHostAddress();
                            }
                        });
                        keepAlive = initParameter("baud_rate", keepAlive, argv[2], new ParameterInitializer<Boolean>() {
                            @Override
                            public Boolean init(String arg) throws Exception {
                                return Boolean.parseBoolean(arg);
                            }
                        });
                    } catch (IndexOutOfBoundsException ie) {
                        //it's ok
                    }
                    System.out.format("Starting Modbus Master TCP with settings:%n\t%s%n, %s", host, keepAlive);
                    master = ModbusMasterFactory.createModbusMasterTCP(host, port, keepAlive);
                    break;
                case RTU:
                    String device_name = SerialPortList.getPortNames()[0];
                    SerialPort.BaudRate baud_rate = SerialPort.BaudRate.BAUD_RATE_115200;
                    int data_bits = 8;
                    int stop_bits = 1;
                    SerialPort.Parity parity = SerialPort.Parity.NONE;

                    try {
                        device_name = initParameter("baud_rate", device_name, argv[1], new ParameterInitializer<String>() {
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
                        parity = initParameter("stop_bits", parity, argv[5], new ParameterInitializer<SerialPort.Parity>() {
                            @Override
                            public SerialPort.Parity init(String arg) throws Exception {
                                return SerialPort.Parity.getParity(Integer.decode(arg));
                            }
                        });
                    } catch (IndexOutOfBoundsException ie) {
                        //it's ok
                    }
                    System.out.format("Starting ModbusMaster RTU with settings:%n\t%s%n, %s, %d, %d, %s%n",
                            device_name, baud_rate.toString(), data_bits, stop_bits, parity.toString());
                    master = ModbusMasterFactory.createModbusMasterRTU(device_name, baud_rate, data_bits, stop_bits, parity);
                    break;
                case ASCII:
                    device_name = SerialPortList.getPortNames()[0];
                    baud_rate = SerialPort.BaudRate.BAUD_RATE_115200;
                    parity = SerialPort.Parity.NONE;
                    try {
                        device_name = initParameter("baud_rate", device_name, argv[1], new ParameterInitializer<String>() {
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
                        parity = initParameter("stop_bits", parity, argv[5], new ParameterInitializer<SerialPort.Parity>() {
                            @Override
                            public SerialPort.Parity init(String arg) throws Exception {
                                return SerialPort.Parity.getParity(Integer.decode(arg));
                            }
                        });
                    } catch (IndexOutOfBoundsException ie) {
                        //it's ok
                    }
                    System.out.format("Starting ModbusMaster ASCII with settings:%n\t%s%n, %s, %s%n",
                            device_name, baud_rate.toString(), parity.toString());
                    master = ModbusMasterFactory.createModbusMasterASCII(device_name, baud_rate, parity);
                    break;
                default:
                    master = ModbusMasterFactory.createModbusMasterTCP("127.0.0.1", false);
            }

            master.setResponseTimeout(1000);

            try {
                master.connect();
            } catch (ModbusIOException e) {
                System.out.format("%s %s%n", "Can't open connection:", e.getLocalizedMessage());
            }

            for (int r = 0; r < 5; r++) {
                try {
                    Thread.sleep(1000);
                    System.out.println();
                    System.out.println("Master output");
                    printRegisters("Holding registers", master.readHoldingRegisters(1, 0, 10));
                    printRegisters("Input registers", master.readInputRegisters(1, 0, 10));
                    printBits("Coils", master.readCoils(1, 0, 10));
                    printBits("Discrete inputs", master.readDiscreteInputs(1, 0, 10));
                    master.writeSingleRegister(1, 0, 69);
                    master.writeSingleCoil(1, 5, true);
                    master.writeMultipleRegisters(1, 1, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 77});
                    master.writeMultipleCoils(1, 0, new boolean[]{true, false, true});
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                master.disconnect();
            } catch (ModbusIOException e) {
                System.out.format("%s %s%n", "Can't close connection:", e.getLocalizedMessage());
            } catch (RuntimeException e) {
                throw e;
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        System.out.format("Usage: %s [%s, %s, %s]%n", ModbusMasterTest.class.getCanonicalName(), "tcp", "rtu", "ascii");
        System.out.format("\t%s additional parameters:%s %s %s%n\t\t%s%n", "tcp",
                "ip address", "port", "keep_alive(true, false)",
                "Example: 127.0.0.1 502 true");
        System.out.format("\t%s additional parameters:%s %s %s %s %s%n\t\t%s%n", "rtu",
                "device_name", "baud_rate", "data_bits", "stop_bits", "parity(none, odd, even, mark, space)",
                "Example: COM1 115200 8 1 none");
        System.out.format("\t%s additional parameters:%s %s %s%n\t\t%s%n", "ascii",
                "device_name", "baud_rate", "parity(none, odd, even, mark, space)",
                "Example: COM1 115200 odd");
    }

    private static void printRegisters(String title, int[] ir) {
        for (int i : ir)
            System.out.format("%6d", i);
        System.out.format("  %s%n", title);
    }

    private static void printBits(String title, boolean[] ir) {
        for (boolean i : ir)
            System.out.format("%6s", i);
        System.out.format("  %s%n", title);
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
