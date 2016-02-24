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
import com.invertor.modbus.msg.base.mei.MEIReadDeviceIdentification;
import com.invertor.modbus.msg.base.mei.ReadDeviceIdentificationCode;
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

    protected ModbusResponse processRequest(ModbusRequest request) throws ModbusProtocolException,
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

    abstract public CommStatus getCommEventLog(int serverAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException;

    /**
     * The data passed in the request data field is to be returned (looped back) in the response. The
     * entire response message should be identical to the request.
     *
     * @param serverAddress a slave address
     * @param queryData     request data field
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public void diagnosticsReturnQueryData(int serverAddress, int queryData) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The remote device serial line port must be initialized and restarted, and all of its
     * communications event counters are cleared. If the port is currently in Listen Only Mode, no
     * response is returned. This function is the only one that brings the port out of Listen Only
     * Mode. If the port is not currently in Listen Only Mode, a normal response is returned. This
     * occurs before the restart is executed.
     * When the remote device receives the request, it attempts a restart and executes its power–up
     * confidence tests. Successful completion of the tests will bring the port online.
     * A request data field contents of FF 00 hex causes the port’s Communications Event Log to be
     * cleared also. Contents of 00 00 leave the log as it was prior to the restart.
     *
     * @param serverAddress a slave address
     * @param clearLog      causes the port’s Communications Event Log to be cleared
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public void diagnosticsRestartCommunicationsOption(int serverAddress, boolean clearLog) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * Returns the contents of the remote device’s 16–bit diagnostic register are returned in the response.
     *
     * @param serverAddress a slave address
     * @return 16–bit diagnostic register
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public int diagnosticsReturnDiagnosticRegister(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The character passed in the request data field becomes the end of message delimiter
     * for future messages (replacing the default LF character). This function is useful in cases of a
     * Line Feed is not required at the end of ASCII messages.
     *
     * @param serverAddress a slave address
     * @param delimiter     request data field
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public void diagnosticsChangeAsciiInputDelimiter(int serverAddress, int delimiter) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * Forces the addressed remote device to its Listen Only Mode for MODBUS communications.
     * This isolates it from the other devices on the network, allowing them to continue
     * communicating without interruption from the addressed remote device. No response is
     * returned.
     * When the remote device enters its Listen Only Mode, all active communication controls are
     * turned off. The Ready watchdog timer is allowed to expire, locking the controls off. While the
     * device is in this mode, any MODBUS messages addressed to it or broadcast are monitored,
     * but no actions will be taken and no responses will be sent.
     * The only function that will be processed after the mode is entered will be the Restart
     * Communications Option function (function code 8, sub-function 1).
     *
     * @param serverAddress a slave address
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public void diagnosticsForceListenOnlyMode(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The goal is to clear all counters and the diagnostic register. Counters are also cleared upon power–up.
     *
     * @param serverAddress a slave address
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public void diagnosticsClearCountersAndDiagnosticRegister(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The response data field returns the quantity of messages that the remote device has detected
     * on the communications system since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return bus message count
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public int diagnosticsReturnBusMessageCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The response data field returns the quantity of CRC errors encountered by the remote device
     * since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return CRC error count
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public int diagnosticsReturnBusCommunicationErrorCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The response data field returns the quantity of MODBUS exception responses returned by the
     * remote device since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return MODBUS exception response count
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public int diagnosticsReturnBusExceptionErrorCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The response data field returns the quantity of messages addressed to the remote device, or
     * broadcast, that the remote device has processed since its last restart, clear counters
     * operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return count of messages has been processed
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public int diagnosticsReturnSlaveMessageCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The response data field returns the quantity of messages addressed to the remote device for
     * which it has returned no response (neither a normal response nor an exception response),
     * since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return no-response count
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public int diagnosticsReturnSlaveNoResponseCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The response data field returns the quantity of messages addressed to the remote device for
     * which it returned a Negative Acknowledge (NAK) exception response, since its last restart,
     * clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return NAK count
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public int diagnosticsReturnSlaveNAKCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The response data field returns the quantity of messages addressed to the remote device for
     * which it returned a Slave Device Busy exception response, since its last restart, clear
     * counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return Slave Device Busy exception response count
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public int diagnosticsReturnSlaveBusyCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * The response data field returns the quantity of messages addressed to the remote device that
     * it could not handle due to a character overrun condition, since its last restart, clear counters
     * operation, or power–up. A character overrun is caused by data characters arriving at the port
     * faster than they can be stored, or by the loss of a character due to a hardware malfunction.
     *
     * @param serverAddress a slave address
     * @return character overrun count
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public int diagnosticsReturnBusCharacterOverrunCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    /**
     * Clears the overrun error counter and reset the error flag.
     *
     * @param serverAddress a slave address
     * @throws ModbusNumberException if server address is in-valid
     */
    abstract public void diagnosticsClearOverrunCounterAndFlag(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException;

    final public MEIReadDeviceIdentification readDeviceIdentification(int serverAddress, int objectId, ReadDeviceIdentificationCode readDeviceId) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        EncapsulatedInterfaceTransportResponse response = (EncapsulatedInterfaceTransportResponse) processRequest(requestFactory.createReadDeviceIdentification(serverAddress, objectId, readDeviceId));
        return (MEIReadDeviceIdentification) response.getMei();
    }
}
