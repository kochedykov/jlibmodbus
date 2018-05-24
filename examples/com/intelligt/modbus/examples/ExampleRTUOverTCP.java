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
 * Authors: Browncrane
 * email: im2busy@qq.com
 */
package com.intelligt.modbus.examples;

import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters;
import com.intelligt.modbus.jlibmodbus.exception.*;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
import com.intelligt.modbus.jlibmodbus.serial.*;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlave;
import com.intelligt.modbus.jlibmodbus.slave.ModbusSlaveFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.intelligt.modbus.jlibmodbus.utils.DataUtils;
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ExampleRTUOverTCP {
    static public void main(String[] argv) {
        try {
            TcpParameters tcpParameter = new TcpParameters();
            InetAddress host = InetAddress.getLocalHost();
            tcpParameter.setHost(host);
            tcpParameter.setPort(2048);
            tcpParameter.setKeepAlive(true);
            SerialUtils.setSerialPortFactory(new SerialPortFactoryTcpServer(tcpParameter));
            SerialParameters serialParameter = new SerialParameters();
            serialParameter.setBaudRate(SerialPort.BaudRate.BAUD_RATE_9600);
            ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveRTU(serialParameter);
            slave.setServerAddress(1);
            ModbusHoldingRegisters holdingRegisters = new ModbusHoldingRegisters(1000);

            for (int i = 0; i < holdingRegisters.getQuantity(); i++) {
                holdingRegisters.set(i, i + 1);
            }
            //place the number PI at offset 0
            holdingRegisters.setFloat64At(0, Math.PI);
            slave.getDataHolder().setHoldingRegisters(holdingRegisters);

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
            slave.listen();
            SerialUtils.setSerialPortFactory(new SerialPortFactoryTcpClient(tcpParameter));
            ModbusMaster master = ModbusMasterFactory.createModbusMasterRTU(serialParameter);
            master.connect();

            int slaveId = 1;
            int offset = 0;
            int quantity = 10;
            //you can invoke #connect method manually, otherwise it'll be invoked automatically
            // at next string we receive ten registers from a slave with id of 1 at offset of 0.
            int[] registerValues = master.readHoldingRegisters(slaveId, offset, quantity);
            // print values
            int address = offset;
            for (int value : registerValues) {
                System.out.println("Address: " + address++ + ", Value: " + value);
            }

            System.out.println("Read " + quantity + " HoldingRegisters start from " + offset);
            System.out.println();

            /*
             * The same thing using a request
             */
            ReadHoldingRegistersRequest readRequest = new ReadHoldingRegistersRequest();
            readRequest.setServerAddress(slaveId);
            readRequest.setStartAddress(offset);
            readRequest.setQuantity(quantity);

            master.processRequest(readRequest);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse)readRequest.getResponse();

            for (int value : response.getHoldingRegisters()) {
                System.out.println("Address: " + address++ + ", Value: " + value);
            }

            System.out.println("Read " + quantity + " HoldingRegisters start from " + offset);
            System.out.println();

            master.disconnect();
            slave.shutdown();
        } catch (SerialPortException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        } catch (IllegalDataValueException e) {
            e.printStackTrace();
        } catch (IllegalDataAddressException e) {
            e.printStackTrace();
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        } catch (ModbusProtocolException e) {
            e.printStackTrace();
        }
    }
}
