package com.sbpinvertor.modbus;

import com.sbpinvertor.conn.SerialPortException;
import com.sbpinvertor.modbus.data.ModbusRequestFactory;
import com.sbpinvertor.modbus.data.ModbusResponseFactory;
import com.sbpinvertor.modbus.data.base.ModbusRequest;
import com.sbpinvertor.modbus.data.base.ModbusResponse;
import com.sbpinvertor.modbus.data.response.ReadHoldingRegistersResponse;
import com.sbpinvertor.modbus.exception.ModbusDataException;
import com.sbpinvertor.modbus.exception.ModbusNumberException;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.net.ModbusTransport;
import com.sbpinvertor.modbus.utils.ByteFifo;
import com.sbpinvertor.modbus.utils.DataUtils;

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

    /* TODO: implement all the functions of modbus api
        READ_COILS:
        READ_DISCRETE_INPUTS:
        READ_HOLDING_REGISTERS:
        READ_INPUT_REGISTERS:
        WRITE_SINGLE_COIL:
        WRITE_SINGLE_REGISTER:
        WRITE_MULTIPLE_COILS:
        WRITE_MULTIPLE_REGISTERS:
        REPORT_SLAVE_ID:
        READ_FILE_RECORD:
        WRITE_FILE_RECORD:
    */
    /*
    public boolean[] readCoils(int device, int adr, int quantity) throws SerialPortException, ModbusTransportException {
        ModbusRequestFactory.createReadCoils(adr, quantity);
        return ((AbstractCoilBank) processRequest(device, request)).getCoils(0, quantity);
    }

    public boolean[] readDiscreteInputs(int device, int adr, int quantity) throws SerialPortException, ModbusTransportException {
        ModbusRequestFactory.createReadDiscreteInputs(adr, quantity);
        return ((AbstractCoilBank) processRequest(device, request)).getCoils(0, quantity);
    }
    */

    private ModbusResponse processRequest(ModbusRequest request) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, ModbusDataException {
        resetBuffers();
        request.write(tx);
        transport.send(tx);
        transport.recv(rx);
        ModbusResponse response = ModbusResponseFactory.getResponse(rx);
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

    final public int[] readHoldingRegisters(int device, int adr, int count) throws SerialPortException,
            ModbusTransportException, ModbusNumberException, ModbusDataException {
        ModbusResponse response = processRequest(ModbusRequestFactory.createReadHoldingRegisters(device, adr, count));
        //TODO: Сейчас данные хранятся в самом запросе, нужно хранить данные в хранилище, чтобы абстрагироваться от конкретного типа ответа
        return DataUtils.toRegistersArray(((ReadHoldingRegistersResponse) response).getValues());
    }

    /*
        public int[] readInputRegisters(int device, int adr, int quantity) throws SerialPortException, ModbusTransportException {
            ModbusRequestFactory.createReadInputRegisters(adr, quantity);
            return ((AbstractRegisterBank) processRequest(device, request)).getRegisters(0, quantity, response);
        }

        public void writeSingleCoil(int device, int adr, boolean flag) throws SerialPortException, ModbusTransportException {
            ModbusRequestFactory.createWriteSingleCoil(adr, flag);
            processRequest(device, request);
        }

        public void writeSingleRegister(int device, int adr, int register) throws SerialPortException, ModbusTransportException {
            ModbusRequestFactory.createWriteSingleRegister(device, adr, register, request);
            processRequest(device, request);
        }
    */


    public void writeMultipleCoils(int device, int adr, byte[] coils) throws SerialPortException, ModbusTransportException {

    }

    public void writeMultipleRegisters(int device, int adr, short[] registers) throws SerialPortException, ModbusTransportException {

    }
}
