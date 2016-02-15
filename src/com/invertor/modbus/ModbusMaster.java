package com.invertor.modbus;

import com.invertor.modbus.data.CommStatus;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.ModbusRequestFactory;
import com.invertor.modbus.msg.base.ModbusFileRecord;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.*;
import com.invertor.modbus.net.ModbusConnection;
import com.invertor.modbus.net.ModbusTransport;
import com.invertor.modbus.utils.ModbusExceptionCode;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
abstract public class ModbusMaster {

    final protected ModbusRequestFactory requestFactory = ModbusRequestFactory.getInstance();

    protected ModbusMaster() {

    }

    abstract protected ModbusConnection getConnection();

    public void open() throws ModbusIOException {
        getConnection().open();
    }

    public void close() throws ModbusIOException {
        getConnection().close();
    }

    protected void sendRequest(ModbusMessage msg) throws ModbusIOException {
        ModbusTransport transport = getConnection().getTransport();
        if (transport == null)
            throw new ModbusIOException("transport is null");
        transport.send(msg);
    }

    protected ModbusMessage readResponse() throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        return getConnection().getTransport().readResponse();
    }

    protected ModbusMessage processRequest(ModbusRequest request) throws ModbusProtocolException,
            ModbusNumberException, ModbusIOException {
        sendRequest(request);
        ModbusResponse msg;
        do {
            try {
                msg = (ModbusResponse) readResponse();
                request.validateResponse(msg);
                if (msg.getModbusExceptionCode() != ModbusExceptionCode.ACKNOWLEDGE) {
                    if (msg.isException())
                        throw new ModbusProtocolException(msg.getModbusExceptionCode());
                    return msg;
                }
            } catch (ModbusNumberException mne) {
                Modbus.log().warning(mne.getLocalizedMessage());
            }
        } while (true);
    }

    /**
     * ModbusMaster will block for only this amount of time.
     * If the timeout expires, a ModbusTransportException is raised, though the ModbusMaster is still valid.
     *
     * @param timeout the specified timeout, in milliseconds.
     */
    public void setResponseTimeout(int timeout) {
        getConnection().setReadTimeout(timeout);
    }

    final public int[] readHoldingRegisters(int serverAddress, int startAddress, int quantity) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReadHoldingRegisters(serverAddress, startAddress, quantity);
        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) processRequest(request);
        return response.getRegisters();
    }

    final public int[] readInputRegisters(int serverAddress, int startAddress, int quantity) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReadInputRegisters(serverAddress, startAddress, quantity);
        ReadHoldingRegistersResponse response = (ReadInputRegistersResponse) processRequest(request);
        return response.getRegisters();
    }

    final public boolean[] readCoils(int serverAddress, int startAddress, int quantity) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReadCoils(serverAddress, startAddress, quantity);
        ReadCoilsResponse response = (ReadCoilsResponse) processRequest(request);
        return response.getCoils();
    }

    final public boolean[] readDiscreteInputs(int serverAddress, int startAddress, int quantity) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReadDiscreteInputs(serverAddress, startAddress, quantity);
        ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) processRequest(request);
        return response.getCoils();
    }

    final public void writeSingleCoil(int serverAddress, int startAddress, boolean flag) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(requestFactory.createWriteSingleCoil(serverAddress, startAddress, flag));
    }

    final public void writeSingleRegister(int serverAddress, int startAddress, int register) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(requestFactory.createWriteSingleRegister(serverAddress, startAddress, register));
    }

    final public void writeMultipleCoils(int serverAddress, int startAddress, boolean[] coils) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(requestFactory.createWriteMultipleCoils(serverAddress, startAddress, coils));
    }

    final public void writeMultipleRegisters(int serverAddress, int startAddress, int[] registers) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(requestFactory.createWriteMultipleRegisters(serverAddress, startAddress, registers));
    }

    final public int[] readWriteMultipleRegisters(int serverAddress, int readAddress, int readQuantity, int writeAddress, int[] registers) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReadWriteMultipleRegisters(serverAddress, readAddress, readQuantity, writeAddress, registers);
        ReadWriteMultipleRegistersResponse response = (ReadWriteMultipleRegistersResponse) processRequest(request);
        return response.getRegisters();
    }

    final public int[] readFifoQueue(int serverAddress, int fifoPointerAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReadFifoQueue(serverAddress, fifoPointerAddress);
        ReadFifoQueueResponse response = (ReadFifoQueueResponse) processRequest(request);
        return response.getFifoValueRegister();
    }

    final public ModbusFileRecord[] readFileRecord(int serverAddress, ModbusFileRecord[] records) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReadFileRecord(serverAddress, records);
        ReadFileRecordResponse response = (ReadFileRecordResponse) processRequest(request);
        return response.getFileRecords();
    }

    final public void writeFileRecord(int serverAddress, ModbusFileRecord record) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(requestFactory.createWriteFileRecord(serverAddress, record));
    }

    /**
     * result = ((reg & and) | (or & !and))
     *
     * @param serverAddress slave id
     * @param startAddress  reference address
     * @param and           the AND mask
     * @param or            the OR mask
     * @throws ModbusProtocolException
     * @throws ModbusNumberException
     * @throws ModbusIOException
     */
    final public void maskWriteRegister(int serverAddress, int startAddress, int and, int or) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(requestFactory.createMaskWriteRegister(serverAddress, startAddress, and, or));
    }

    abstract public int readExceptionStatus(int serverAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException;

    abstract public byte[] reportSlaveId(int serverAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException;

    abstract public CommStatus getCommEventCounter(int serverAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException;
}
