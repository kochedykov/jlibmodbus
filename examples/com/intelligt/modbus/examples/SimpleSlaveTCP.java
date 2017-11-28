package com.intelligt.modbus.examples;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataAddressException;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlave;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2017 Vladislav Kochedykov
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
public class SimpleSlaveTCP {

    static public void main(String[] argv) {

        try {

            final ModbusSlave slave;

            TcpParameters tcpParameters = new TcpParameters();

            tcpParameters.setHost(InetAddress.getLocalHost());
            tcpParameters.setKeepAlive(true);
            tcpParameters.setPort(Modbus.TCP_PORT);

            slave = ModbusSlaveFactory.createModbusSlaveTCP(tcpParameters);
            Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);

            MyOwnDataHolder dh = new MyOwnDataHolder();
            dh.addEventListener(new ModbusEventListener() {
                @Override
                public void onWriteToSingleCoil(int address, boolean value) {
                    System.out.print("onWriteToSingleCoil: address " + address + ", value " + value);
                }

                @Override
                public void onWriteToMultipleCoils(int address, int quantity, boolean[] values) {
                    System.out.print("onWriteToMultipleCoils: address " + address + ", quantity " + quantity);
                }

                @Override
                public void onWriteToSingleHoldingRegister(int address, int value) {
                    System.out.print("onWriteToSingleHoldingRegister: address " + address + ", value " + value);
                }

                @Override
                public void onWriteToMultipleHoldingRegisters(int address, int quantity, int[] values) {
                    System.out.print("onWriteToMultipleHoldingRegisters: address " + address + ", quantity " + quantity);
                }
            });

            slave.setDataHolder(dh);
            ModbusHoldingRegisters hr = new ModbusHoldingRegisters(10);
            hr.set(0, 12345);
            slave.getDataHolder().setHoldingRegisters(hr);
            slave.setServerAddress(1);
            /*
             * using master-branch it should be #slave.open();
             */
            slave.listen();

            /*
             * since 1.2.8
             */
            if (slave.isListening()) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        synchronized (slave) {
                            slave.notifyAll();
                        }
                    }
                });

                synchronized (slave) {
                    slave.wait();
                }

                /*
                 * using master-branch it should be #slave.close();
                 */
                slave.shutdown();
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ModbusEventListener {
        void onWriteToSingleCoil(int address, boolean value);

        void onWriteToMultipleCoils(int address, int quantity, boolean[] values);

        void onWriteToSingleHoldingRegister(int address, int value);

        void onWriteToMultipleHoldingRegisters(int address, int quantity, int[] values);
    }

    public static class MyOwnDataHolder extends DataHolder {

        final List<ModbusEventListener> modbusEventListenerList = new ArrayList<ModbusEventListener>();

        public MyOwnDataHolder() {
            // you can place the initialization code here
            /*
            something like that:
            setHoldingRegisters(new SimpleHoldingRegisters(10));
            setCoils(new Coils(128));
            ...
            etc.
             */
        }

        public void addEventListener(ModbusEventListener listener) {
            modbusEventListenerList.add(listener);
        }

        public boolean removeEventListener(ModbusEventListener listener) {
            return modbusEventListenerList.remove(listener);
        }

        @Override
        public void writeHoldingRegister(int offset, int value) throws IllegalDataAddressException, IllegalDataValueException {
            for (ModbusEventListener l : modbusEventListenerList) {
                l.onWriteToSingleHoldingRegister(offset, value);
            }
            super.writeHoldingRegister(offset, value);
        }

        @Override
        public void writeHoldingRegisterRange(int offset, int[] range) throws IllegalDataAddressException, IllegalDataValueException {
            for (ModbusEventListener l : modbusEventListenerList) {
                l.onWriteToMultipleHoldingRegisters(offset, range.length, range);
            }
            super.writeHoldingRegisterRange(offset, range);
        }

        @Override
        public void writeCoil(int offset, boolean value) throws IllegalDataAddressException, IllegalDataValueException {
            for (ModbusEventListener l : modbusEventListenerList) {
                l.onWriteToSingleCoil(offset, value);
            }
            super.writeCoil(offset, value);
        }

        @Override
        public void writeCoilRange(int offset, boolean[] range) throws IllegalDataAddressException, IllegalDataValueException {
            for (ModbusEventListener l : modbusEventListenerList) {
                l.onWriteToMultipleCoils(offset, range.length, range);
            }
            super.writeCoilRange(offset, range);
        }
    }
}
