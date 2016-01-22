package com.sbpinvertor.modbus;

import com.sbpinvertor.conn.SerialPortException;
import com.sbpinvertor.modbus.data.ModbusRequestFactory;
import com.sbpinvertor.modbus.data.base.ModbusRequest;
import com.sbpinvertor.modbus.data.base.ModbusResponse;
import com.sbpinvertor.modbus.data.response.ReadCoilsResponse;
import com.sbpinvertor.modbus.data.response.ReadDiscreteInputsResponse;
import com.sbpinvertor.modbus.data.response.ReadHoldingRegistersResponse;
import com.sbpinvertor.modbus.data.response.ReadInputRegistersResponse;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusProtocolException;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.net.ModbusTransport;
import com.sbpinvertor.modbus.utils.ByteFifo;

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
    final private ByteFifo rx = new ByteFifo(Modbus.MAX_RTU_ADU_LENGTH);
    final private ByteFifo tx = new ByteFifo(Modbus.MAX_RTU_ADU_LENGTH);

    ModbusMaster(ModbusTransport transport) {
        this.transport = transport;
    }

    private ModbusResponse processRequest(ModbusRequest request) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, ModbusProtocolException, IOException {
        resetBuffers();
        ModbusResponse response = transport.sendRequest(request);
        if (request.getServerAddress() != response.getServerAddress())
            throw new ModbusTransportException("Collision: does not matches the slave address");
        if (request.getFunction() != response.getFunction())
            throw new ModbusTransportException("Collision: does not matches the function code");
        return response;
    }

    private void resetBuffers() {
        tx.clear();
        rx.clear();
    }

    final public int[] readHoldingRegisters(int serverAddress, int startAddress, int quantity) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, ModbusProtocolException, IOException {
        ModbusResponse response = processRequest(ModbusRequestFactory.createReadHoldingRegisters(serverAddress, startAddress, quantity));
        return ((ReadHoldingRegistersResponse) response).getRegisters();
    }


    final public int[] readInputRegisters(int serverAddress, int startAddress, int quantity) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException, ModbusProtocolException {
        ModbusResponse response = processRequest(ModbusRequestFactory.createReadInputRegisters(serverAddress, startAddress, quantity));
        return ((ReadInputRegistersResponse) response).getRegisters();
    }

    final public void writeSingleCoil(int serverAddress, int startAddress, boolean flag) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException, ModbusProtocolException {
        processRequest(ModbusRequestFactory.createWriteSingleCoil(serverAddress, startAddress, flag));
    }

    final public void writeSingleRegister(int serverAddress, int startAddress, int register) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException, ModbusProtocolException {
        processRequest(ModbusRequestFactory.createWriteSingleRegister(serverAddress, startAddress, register));
    }

    final public void writeMultipleCoils(int serverAddress, int startAddress, boolean[] coils) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException, ModbusProtocolException {
        processRequest(ModbusRequestFactory.createWriteMultipleCoils(serverAddress, startAddress, coils));
    }

    final public void writeMultipleRegisters(int serverAddress, int startAddress, int[] registers) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException, ModbusProtocolException {
        processRequest(ModbusRequestFactory.createWriteMultipleRegisters(serverAddress, startAddress, registers));
    }

    final public boolean[] readCoils(int serverAddress, int startAddress, int quantity) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException, ModbusProtocolException {
        ModbusResponse response = processRequest(ModbusRequestFactory.createReadCoils(serverAddress, startAddress, quantity));
        return ((ReadCoilsResponse) response).getCoils();
    }

    final public boolean[] readDiscreteInputs(int serverAddress, int startAddress, int quantity) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, IOException, ModbusProtocolException {
        ModbusResponse response = processRequest(ModbusRequestFactory.createReadDiscreteInputs(serverAddress, startAddress, quantity));
        return ((ReadDiscreteInputsResponse) response).getCoils();
    }
}
