package com.invertor.examples.modbus;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.ModbusMaster;
import com.invertor.modbus.ModbusMasterFactory;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.tcp.TcpParameters;

import java.net.InetAddress;

/**
 * Copyright (c) 2016, JSC Invertor
 * All rights reserved
 * <p/>
 * This file is part of JLibModbus.
 * <p/>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * <p/>
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <p/>
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * <p/>
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class SimpleMasterTCP {

    static public void main(String [] args) {
        try {
            TcpParameters tcpParameters = new TcpParameters();

            //tcp parameters have already set by default as in example
            tcpParameters.setHost(InetAddress.getLocalHost());
            tcpParameters.setKeepAlive(true);
            tcpParameters.setPort(Modbus.TCP_PORT);

            //if you would like to set connection parameters separately,
            // you should use another method: createModbusMasterTCP(String host, int port, boolean keepAlive);
            ModbusMaster m = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
            Modbus.setAutoIncrementTransactionId(true);

            int slaveId = 1;
            int offset = 0;
            int quantity = 10;
            try {
                // at next string we receive ten registers from a slave with id of 1 at offset of 0.
                int[] registerValues = m.readHoldingRegisters(slaveId, offset, quantity);
                // let's print them all :)
                for (int value : registerValues) {
                    System.out.println("Address: " + offset++ + ", Value: " + value);
                }
            } catch (ModbusProtocolException e) {
                e.printStackTrace();
            } catch (ModbusNumberException e) {
                e.printStackTrace();
            } catch (ModbusIOException e) {
                e.printStackTrace();
            } finally {
                try {
                    m.close();
                } catch (ModbusIOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
