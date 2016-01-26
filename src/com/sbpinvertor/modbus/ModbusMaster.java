package com.sbpinvertor.modbus;

import com.sbpinvertor.conn.SerialPortException;
import com.sbpinvertor.modbus.data.ModbusMessageFactory;
import com.sbpinvertor.modbus.data.ModbusRequestFactory;
import com.sbpinvertor.modbus.data.ModbusResponseFactory;
import com.sbpinvertor.modbus.data.base.ModbusMessage;
import com.sbpinvertor.modbus.data.response.ReadCoilsResponse;
import com.sbpinvertor.modbus.data.response.ReadDiscreteInputsResponse;
import com.sbpinvertor.modbus.data.response.ReadHoldingRegistersResponse;
import com.sbpinvertor.modbus.data.response.ReadInputRegistersResponse;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.net.ModbusTransport;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ModbusMaster {

    final private ModbusTransport transport;
    final private ModbusRequestFactory requestFactory = ModbusRequestFactory.getInstance();
    final private ModbusMessageFactory responseFactory = ModbusResponseFactory.getInstance();

    ModbusMaster(ModbusTransport transport) {
        this.transport = transport;
    }

    private ModbusMessage processRequest(ModbusMessage request) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException {
        transport.send(request);
        ModbusMessage response = transport.recv(responseFactory);
        if (request.getServerAddress() != response.getServerAddress())
            throw new ModbusTransportException("Collision: does not matches the slave address");
        if (request.getFunction() != response.getFunction())
            throw new ModbusTransportException("Collision: does not matches the function code");
        return response;
    }

    /**
     * ModbusMaster will block for only this amount of time.
     * If the timeout expires, a ModbusTransportException is raised, though the ModbusMaster is still valid.
     *
     * @param timeout the specified timeout, in milliseconds.
     */
    public void setResponseTimeout(int timeout) {
        transport.setResponseTimeout(timeout);
    }

    final public int[] readHoldingRegisters(int serverAddress, int startAddress, int quantity) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException {
        ModbusMessage request = requestFactory.createReadHoldingRegisters(serverAddress, startAddress, quantity);
        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) processRequest(request);
        return response.getRegisters();
    }


    final public int[] readInputRegisters(int serverAddress, int startAddress, int quantity) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException {
        ModbusMessage request = requestFactory.createReadInputRegisters(serverAddress, startAddress, quantity);
        ReadHoldingRegistersResponse response = (ReadInputRegistersResponse) processRequest(request);
        return response.getRegisters();
    }

    final public boolean[] readCoils(int serverAddress, int startAddress, int quantity) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException {
        ModbusMessage request = requestFactory.createReadCoils(serverAddress, startAddress, quantity);
        ReadCoilsResponse response = (ReadCoilsResponse) processRequest(request);
        return response.getCoils();
    }

    final public boolean[] readDiscreteInputs(int serverAddress, int startAddress, int quantity) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException {
        ModbusMessage request = requestFactory.createReadDiscreteInputs(serverAddress, startAddress, quantity);
        ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) processRequest(request);
        return response.getCoils();
    }

    final public void writeSingleCoil(int serverAddress, int startAddress, boolean flag) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException {
        processRequest(requestFactory.createWriteSingleCoil(serverAddress, startAddress, flag));
    }

    final public void writeSingleRegister(int serverAddress, int startAddress, int register) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException {
        processRequest(requestFactory.createWriteSingleRegister(serverAddress, startAddress, register));
    }

    final public void writeMultipleCoils(int serverAddress, int startAddress, boolean[] coils) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException {
        processRequest(requestFactory.createWriteMultipleCoils(serverAddress, startAddress, coils));
    }

    final public void writeMultipleRegisters(int serverAddress, int startAddress, int[] registers) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException {
        processRequest(requestFactory.createWriteMultipleRegisters(serverAddress, startAddress, registers));
    }
}
