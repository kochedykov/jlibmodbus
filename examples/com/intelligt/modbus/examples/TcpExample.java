package com.intelligt.modbus.examples;

import com.intelligt.modbus.jlibmodbus.*;
import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * Copyright (C) 2017 Vladislav Y. Kochedykov
 * [https://github.com/kochedykov/jlibmodbus]
 *
 * This file is part of JLibModbus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class TcpExample {
    static public void main(String[] argv) {

        //
        try {
            TcpParameters tcpParameters = new TcpParameters();
            //listening on all interfaces
            tcpParameters.setHost(InetAddress.getByName("0.0.0.0"));
            tcpParameters.setPort(Modbus.TCP_PORT);
            tcpParameters.setKeepAlive(true);

            ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveTCP(tcpParameters);
            ModbusMaster master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);

            master.setResponseTimeout(1000);
            slave.setServerAddress(Modbus.TCP_DEFAULT_ID);
            slave.setBroadcastEnabled(true);
            slave.setReadTimeout(10000);

            ModbusHoldingRegisters holdingRegisters = new ModbusHoldingRegisters(1000);

            for (int i = 0; i < holdingRegisters.getQuantity(); i++) {
                //fill
                holdingRegisters.set(i, i + 1);
            }

            //place the number PI at offset 0
            holdingRegisters.setFloat64At(0, Math.PI);

            slave.getDataHolder().setHoldingRegisters(holdingRegisters);

            Modbus.setAutoIncrementTransactionId(true);

            slave.listen();

            master.connect();

            //prepare request
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest();
            request.setServerAddress(Modbus.TCP_DEFAULT_ID);
            request.setStartAddress(0);
            request.setQuantity(10);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) request.getResponse();

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
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ModbusProtocolException e) {
            e.printStackTrace();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        }
    }
}
