package com.intelligt.modbus.examples;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
import com.intelligt.modbus.jlibmodbus.serial.*;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlave;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveFactory;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/*
 * Copyright (C) 2018 "Invertor" Factory", JSC
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
 * Authors: Vladislav Kochedykov.
 * email: vladislav.kochedykov@gmail.com
 */
public class ExampleRTU {

    final static private int slaveId = 1;

    public static void main(String[] argv) {
        try {
            Modbus.log().addHandler(new Handler() {
                @Override
                public void publish(LogRecord record) {
                    System.out.println(record.getLevel().getName() + ": " + record.getMessage());
                }

                @Override
                public void flush() {
                    //do nothing
                }

                @Override
                public void close() throws SecurityException {
                    //do nothing
                }
            });
            Modbus.setLogLevel(Modbus.LogLevel.LEVEL_RELEASE);
            SerialParameters slaveSerialParameters = new SerialParameters();
            SerialParameters masterSerialParameters = new SerialParameters();

            SerialUtils.trySelectConnector();
            /*
             Use a virtual serial port SerialPortLoopback
             */
            //SerialUtils.setSerialPortFactory(new SerialPortFactoryLoopback(false));
            SerialUtils.setSerialPortFactory(new SerialPortFactoryJSerialComm());

            slaveSerialParameters.setDevice("COM2");
            slaveSerialParameters.setParity(SerialPort.Parity.NONE);
            SerialPort.BaudRate baudrate = new SerialPort.BaudRate(921600);
            slaveSerialParameters.setBaudRate(baudrate);
            slaveSerialParameters.setDataBits(8);
            slaveSerialParameters.setStopBits(1);

            ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveRTU(slaveSerialParameters);


            //SerialUtils.setSerialPortFactory(new SerialPortFactoryLoopback(true));
            masterSerialParameters.setDevice("COM3");
            masterSerialParameters.setParity(SerialPort.Parity.NONE);
            masterSerialParameters.setBaudRate(baudrate);
            masterSerialParameters.setDataBits(8);
            masterSerialParameters.setStopBits(1);
            ModbusMaster master = ModbusMasterFactory.createModbusMasterRTU(masterSerialParameters);

            master.setResponseTimeout(1000);

            slave.setServerAddress(slaveId);
            slave.setBroadcastEnabled(true);
            slave.setReadTimeout(1000);

        /*    CompletableFuture<ReadHoldingRegistersResponse> future =
                    master.sendRequest(new ReadHoldingRegistersRequest(0, 10), 0);*/

            FrameEventListener listener = new FrameEventListener() {
                @Override
                public void frameSentEvent(FrameEvent event) {
                    System.out.println("frame sent " + DataUtils.toAscii(event.getBytes()));
                }

                @Override
                public void frameReceivedEvent(FrameEvent event) {
                    System.out.println("frame recv " + DataUtils.toAscii(event.getBytes()));
                }
            };

            slave.addListener(listener);
            master.addListener(listener);

            ModbusHoldingRegisters holdingRegisters = new ModbusHoldingRegisters(1000);

            for (int i = 0; i < holdingRegisters.getQuantity(); i++) {
                //fill
                holdingRegisters.set(i, i + 1);
            }

            //place the number PI at offset 0
            holdingRegisters.setFloat64At(0, Math.PI);

            slave.getDataHolder().setHoldingRegisters(holdingRegisters);

            slave.listen();

            Thread.sleep(1000);

            master.connect();

            //prepare request
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest();
            request.setServerAddress(1);
            request.setStartAddress(0);
            request.setQuantity(10);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) request.getResponse();

            //TODO: ReadHoldingRegistersResponse response = master.processRequestAsync(request);
            //CompletableFuture<ReadHoldingRegistersResponse> future
            master.processRequest(request);
            ModbusHoldingRegisters registers = response.getHoldingRegisters();
            for (int r : registers) {
                System.out.println(r);
            }
            //get float
            System.out.println("PI is approximately equal to " + registers.getFloat64At(0));
            System.out.println();

            master.disconnect();
            slave.shutdown();
        } catch (ModbusProtocolException e) {
            e.printStackTrace();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        } catch (SerialPortException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
