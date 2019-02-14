package com.intelligt.modbus.jlibmodbus.master;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.CommStatus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.msg.ModbusRequestBuilder;
import com.intelligt.modbus.jlibmodbus.msg.ModbusRequestFactory;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusFileRecord;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusMessage;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.msg.base.mei.MEIReadDeviceIdentification;
import com.intelligt.modbus.jlibmodbus.msg.base.mei.ReadDeviceIdentificationCode;
import com.intelligt.modbus.jlibmodbus.msg.response.*;
import com.intelligt.modbus.jlibmodbus.net.ModbusConnection;
import com.intelligt.modbus.jlibmodbus.net.transport.ModbusTransport;
import com.intelligt.modbus.jlibmodbus.utils.FrameEvent;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListener;
import com.intelligt.modbus.jlibmodbus.utils.FrameEventListenerList;
import com.intelligt.modbus.jlibmodbus.utils.ModbusExceptionCode;

/*
 * Copyright (C) 2016 "Invertor" Factory", JSC
 * [http://www.sbp-invertor.ru]
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
abstract public class ModbusMaster implements FrameEventListenerList {

    final private ModbusConnection conn;
    final private BroadcastResponse broadcastResponse = new BroadcastResponse();
    private int transactionId = 0;
    private long requestTime = 0;

    public ModbusMaster(ModbusConnection conn) {
        this.conn = conn;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = Math.min(Math.abs(transactionId), Modbus.TRANSACTION_ID_MAX_VALUE);
    }

    protected ModbusConnection getConnection() {
        return conn;
    }

    /**
     * this method allows you to implement your own behavior of connect method.
     *
     * @throws ModbusIOException
     */
    protected void connectImpl() throws ModbusIOException {
        getConnection().open();
    }

    /**
     * this method allows you to implement your own behavior of disconnect method.
     *
     * @throws ModbusIOException
     */
    protected void disconnectImpl() throws ModbusIOException {
        getConnection().close();
    }

    final public void connect() throws ModbusIOException {
        if (!isConnected()) {
            connectImpl();
        }
    }

    final public void disconnect() throws ModbusIOException {
        if (isConnected()) {
            disconnectImpl();
        }
    }

    public boolean isConnected() {
        return getConnection().isOpened();
    }

    protected void sendRequest(ModbusMessage msg) throws ModbusIOException {
        ModbusTransport transport = getConnection().getTransport();
        if (transport == null)
            throw new ModbusIOException("transport is null");
        transport.send(msg);
        requestTime = System.currentTimeMillis();
    }

    protected ModbusMessage readResponse(ModbusRequest request) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        return getConnection().getTransport().readResponse(request);
    }

    /**
     * this function allows you to process your own ModbusRequest.
     * Each request class has a compliant response class for instance ReadHoldingRegistersRequest and ReadHoldingRegistersResponse.
     * If you process an instance of ReadHoldingRegistersRequest this function exactly either returns a ReadHoldingRegistersResponse instance or throws an exception.
     *
     * @param request an instance of ModbusRequest.
     * @return an instance of ModbusResponse.
     * @throws ModbusProtocolException
     * @throws ModbusIOException
     * @see ModbusRequestFactory
     * @see ModbusRequest
     * @see ModbusResponse
     * @see com.intelligt.modbus.jlibmodbus.msg.request
     * @see com.intelligt.modbus.jlibmodbus.msg.request
     */
    synchronized public ModbusResponse processRequest(ModbusRequest request) throws ModbusProtocolException, ModbusIOException {
        try {
            sendRequest(request);
            if (request.getServerAddress() != Modbus.BROADCAST_ID) {
                do {
                    try {
                        ModbusResponse msg = (ModbusResponse) readResponse(request);
                        request.validateResponse(msg);
                        /*
                         * if you have received an ACKNOWLEDGE,
                         * it means that operation is in processing and you should be waiting for the answer
                         */
                        if (msg.getModbusExceptionCode() != ModbusExceptionCode.ACKNOWLEDGE) {
                            if (msg.isException())
                                throw new ModbusProtocolException(msg.getModbusExceptionCode());
                            return msg;
                        }
                    } catch (ModbusNumberException mne) {
                        Modbus.log().warning(mne.getLocalizedMessage());
                    }
                } while (System.currentTimeMillis() - requestTime < getConnection().getReadTimeout());
                /*
                 * throw an exception if there is a response timeout
                 */
                throw new ModbusIOException("Response timeout.");
            } else {
            /*
             return because slaves do not respond broadcast requests
             */
                broadcastResponse.setFunction(request.getFunction());
                return broadcastResponse;
            }
        } catch (ModbusIOException mioe) {
            disconnect();
            throw mioe;
        }
    }

    /**
     * ModbusMaster will block for only this amount of time.
     * If the timeout expires, a ModbusTransportException is raised, though the ModbusMaster is still valid.
     *
     * @param timeout the specified timeout, in milliseconds.
     */
    public void setResponseTimeout(int timeout) {
        try {
            getConnection().setReadTimeout(timeout);
        } catch (Exception e) {
            Modbus.log().warning(e.getLocalizedMessage());
        }
    }

    /**
     * This function code is used to read the contents of a contiguous block of holding registers in a
     * remote device. The Request PDU specifies the starting register address and the number of
     * registers. In the PDU Registers are addressed starting at zero. Therefore registers numbered
     * 1-16 are addressed as 0-15.
     *
     * @param serverAddress a slave address
     * @param startAddress  starting register address
     * @param quantity      the number of registers
     * @return the register data
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public int[] readHoldingRegisters(int serverAddress, int startAddress, int quantity) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReadHoldingRegisters(serverAddress, startAddress, quantity);
        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) processRequest(request);
        return response.getRegisters();
    }

    /**
     * This function code is used to read from 1 to 125 contiguous input registers in a remote
     * device. The Request PDU specifies the starting register address and the number of registers.
     * In the PDU Registers are addressed starting at zero. Therefore input registers numbered 1-16
     * are addressed as 0-15.
     *
     * @param serverAddress a slave address
     * @param startAddress  starting register address
     * @param quantity      the number of registers
     * @return the register data
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public int[] readInputRegisters(int serverAddress, int startAddress, int quantity) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReadInputRegisters(serverAddress, startAddress, quantity);
        ReadHoldingRegistersResponse response = (ReadInputRegistersResponse) processRequest(request);
        return response.getRegisters();
    }

    /**
     * This function code is used to read from 1 to 2000 contiguous status of coils in a remote
     * device. The Request PDU specifies the starting address, i.e. the address of the first coil
     * specified, and the number of coils. In the PDU Coils are addressed starting at zero. Therefore
     * coils numbered 1-16 are addressed as 0-15.
     * If the returned output quantity is not a multiple of eight, the remaining coils in the final boolean array
     * will be padded with FALSE.
     *
     * @param serverAddress a slave address
     * @param startAddress  the address of the first coil
     * @param quantity      the number of coils
     * @return the coils
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final synchronized public boolean[] readCoils(int serverAddress, int startAddress, int quantity) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReadCoils(serverAddress, startAddress, quantity);
        ReadCoilsResponse response = (ReadCoilsResponse) processRequest(request);
        return response.getCoils();
    }

    /**
     * This function code is used to read from 1 to 2000 contiguous status of discrete inputs in a
     * remote device. The Request PDU specifies the starting address, i.e. the address of the first
     * input specified, and the number of inputs. In the PDU Discrete Inputs are addressed starting
     * at zero. Therefore Discrete inputs numbered 1-16 are addressed as 0-15.
     * If the returned input quantity is not a multiple of eight, the remaining inputs in the final boolean array
     * will be padded with FALSE.
     *
     * @param serverAddress a slave address
     * @param startAddress  the address of the first input
     * @param quantity      the number of inputs
     * @return the discrete inputs
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public boolean[] readDiscreteInputs(int serverAddress, int startAddress, int quantity) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReadDiscreteInputs(serverAddress, startAddress, quantity);
        ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) processRequest(request);
        return response.getCoils();
    }

    /**
     * This function code is used to write a single output to either ON or OFF in a remote device.
     * The requested ON/OFF state is specified by a constant in the request data field. A value of
     * TRUE requests the output to be ON. A value of FALSE requests it to be OFF.
     * The Request PDU specifies the address of the coil to be forced. Coils are addressed starting
     * at zero. Therefore coil numbered 1 is addressed as 0.
     *
     * @param serverAddress a slave address
     * @param startAddress  the address of the coil to be forced
     * @param flag          the request data field
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public void writeSingleCoil(int serverAddress, int startAddress, boolean flag) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildWriteSingleCoil(serverAddress, startAddress, flag));
    }

    /**
     * This function code is used to write a single holding register in a remote device.
     * The Request PDU specifies the address of the register to be written. Registers are addressed
     * starting at zero. Therefore register numbered 1 is addressed as 0.
     *
     * @param serverAddress a slave address
     * @param startAddress  the address of the register to be written
     * @param register      value
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public void writeSingleRegister(int serverAddress, int startAddress, int register) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildWriteSingleRegister(serverAddress, startAddress, register));
    }

    /**
     * This function code is used to force each coil in a sequence of coils to either ON or OFF in a
     * remote device. The Request PDU specifies the coil references to be forced. Coils are
     * addressed starting at zero. Therefore coil numbered 1 is addressed as 0.
     * The requested ON/OFF states are specified by contents of the request array of boolean. A logical 'true'
     * in position of the array requests the corresponding output to be ON. A logical 'false' requests
     * it to be OFF.
     *
     * @param serverAddress a slave address
     * @param startAddress  the address of the coils to be written
     * @param coils         the coils
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public void writeMultipleCoils(int serverAddress, int startAddress, boolean[] coils) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildWriteMultipleCoils(serverAddress, startAddress, coils));
    }

    /**
     * This function code is used to write a block of contiguous registers (1 to 123 registers) in a
     * remote device.
     * The requested written values are specified in the request data field. Data is packed as two
     * bytes per register.
     *
     * @param serverAddress a slave address
     * @param startAddress  the address of the registers to be written
     * @param registers     the register data
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public void writeMultipleRegisters(int serverAddress, int startAddress, int[] registers) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildWriteMultipleRegisters(serverAddress, startAddress, registers));
    }

    /**
     * This function code performs a combination of one read operation and one write operation in a
     * single MODBUS transaction. The write operation is performed before the read.
     * Holding registers are addressed starting at zero. Therefore holding registers 1-16 are
     * addressed in the PDU as 0-15.
     * The request specifies the starting address and number of holding registers to be read as well
     * as the starting address, number of holding registers, and the data to be written.
     *
     * @param serverAddress a slave address
     * @param readAddress   the address of the registers to be read
     * @param readQuantity  the number of registers to be read
     * @param writeAddress  the address of the registers to be written
     * @param registers     the number of registers to be written
     * @return the register value
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public int[] readWriteMultipleRegisters(int serverAddress, int readAddress, int readQuantity, int writeAddress, int[] registers) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReadWriteMultipleRegisters(serverAddress, readAddress, readQuantity, writeAddress, registers);
        ReadWriteMultipleRegistersResponse response = (ReadWriteMultipleRegistersResponse) processRequest(request);
        return response.getRegisters();
    }

    /**
     * This function code allows to read the contents of a First-In-First-Out (FIFO) queue of register
     * in a remote device. The function returns the queued data.
     * Up to 31 queue data registers can be read.
     * The function reads the queue contents, but does not clear them.
     * If the queue count exceeds 31, an exception response is returned with an error code of 03
     * (Illegal Data Value).
     *
     * @param serverAddress      a slave address
     * @param fifoPointerAddress address of a fifo pointer register
     * @return the data register value
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public int[] readFifoQueue(int serverAddress, int fifoPointerAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReadFifoQueue(serverAddress, fifoPointerAddress);
        ReadFifoQueueResponse response = (ReadFifoQueueResponse) processRequest(request);
        return response.getFifoValueRegister();
    }

    /**
     * This function code is used to perform a file record read.
     * A file is an organization of records. Each file contains 10000 records, addressed 0000 to
     * 9999 decimal or 0X0000 to 0X270F. For example, record 12 is addressed as 12.
     * The function can read multiple groups of references.
     *
     * @param serverAddress a slave address
     * @param records       array of ModbusFileRecord
     * @return array of ModbusFileRecord has been read
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public ModbusFileRecord[] readFileRecord(int serverAddress, ModbusFileRecord[] records) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReadFileRecord(serverAddress, records);
        ReadFileRecordResponse response = (ReadFileRecordResponse) processRequest(request);
        return response.getFileRecords();
    }

    /**
     * This function code is used to perform a file record write. All Request Data Lengths are
     * provided in terms of number of bytes and all Record Lengths are provided in terms of the
     * number of 16-bit words.
     * A file is an organization of records. Each file contains 10000 records, addressed 0000 to
     * 9999 decimal or 0X0000 to 0X270F. For example, record 12 is addressed as 12.
     * The function can write multiple groups of references.
     *
     * @param serverAddress a server address
     * @param record        the ModbusFileRecord to be written
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public void writeFileRecord(int serverAddress, ModbusFileRecord record) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildWriteFileRecord(serverAddress, record));
    }

    /**
     * This function code is used to modify the contents of a specified holding register using a
     * combination of an AND mask, an OR mask, and the register's current contents. The function
     * can be used to set or clear individual bits in the register.
     * The request specifies the holding register to be written, the data to be used as the AND
     * mask, and the data to be used as the OR mask. Registers are addressed starting at zero.
     * Therefore registers 1-16 are addressed as 0-15.
     * The function’s algorithm is:
     * Result = (Current Contents AND And_Mask) OR (Or_Mask AND (NOT And_Mask))
     * For example:
     * Hex Binary
     * Current Contents=    12  0001 0010
     * And_Mask =           F2  1111 0010
     * Or_Mask =            25  0010 0101
     * <p>
     * (NOT And_Mask)=      0D  0000 1101
     * <p>
     * Result =             17  0001 0111
     * <p>
     * Note:
     * y If the Or_Mask value is zero, the result is simply the logical ANDing of the current contents and
     * And_Mask. If the And_Mask value is zero, the result is equal to the Or_Mask value.
     * y The contents of the register can be read with the Read Holding Registers function (function code 03).
     * They could, however, be changed subsequently as the controller scans its user logic program.
     *
     * @param serverAddress slave id
     * @param startAddress  reference address
     * @param and           the AND mask
     * @param or            the OR mask
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    final public void maskWriteRegister(int serverAddress, int startAddress, int and, int or) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildMaskWriteRegister(serverAddress, startAddress, and, or));
    }

    /**
     * This function code is used to read the contents of eight Exception Status outputs in a remote
     * device.
     * The function provides a simple method for accessing this information, because the Exception
     * Output references are known (no output reference is needed in the function).
     * The normal response contains the status of the eight Exception Status outputs. The outputs
     * are packed into one data byte, with one bit per output. The status of the lowest output
     * reference is contained in the least significant bit of the byte.
     * The contents of the eight Exception Status outputs are device specific.
     *
     * @param serverAddress a slave address
     * @return the eight Exception Status outputs
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    abstract public int readExceptionStatus(int serverAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException;

    /**
     * This function code is used to read the description of the type, the current status, and other
     * information specific to a remote device.
     * The data contents are specific to each type of device.
     *
     * @param serverAddress slave address
     * @return a byte array of the device's specific data
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     */
    abstract public byte[] reportSlaveId(int serverAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException;

    /**
     * This function code is used to get a status word and an event count from the remote device's
     * communication event counter.
     * By fetching the current count before and after a series of messages, a client can determine
     * whether the messages were handled normally by the remote device.
     * The device’s event counter is incremented once for each successful message completion. It
     * is not incremented for exception responses, poll commands, or fetch event counter
     * commands.
     * The event counter can be reset by means of the Diagnostics function (code 08), with a subfunction of Restart Communications Option (code 00 01) or Clear Counters and Diagnostic
     * Register (code 00 0A).
     *
     * @param serverAddress a slave address
     * @return the CommStatus instance
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     * @see CommStatus
     */
    abstract public CommStatus getCommEventCounter(int serverAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException;

    /**
     * This function code is used to get a status word, event count, message count, and a field of
     * event bytes from the remote device.
     * The status word and event counts are identical to that returned by the Get Communications
     * Event Counter function (11, 0B hex).
     * The message counter contains the quantity of messages processed by the remote device
     * since its last restart, clear counters operation, or power–up. This count is identical to that
     * returned by the Diagnostic function (code 08), sub-function Return Bus Message Count (code
     * 11, 0B hex).
     * The event bytes field contains 0-64 bytes, with each byte corresponding to the status of one
     * MODBUS send or receive operation for the remote device. The remote device enters the
     * events into the field in chronological order. Byte 0 is the most recent event. Each new byte
     * flushes the oldest byte from the field.
     *
     * @param serverAddress a slave address
     * @return the CommStatus instance
     * @throws ModbusProtocolException if modbus-exception is received
     * @throws ModbusNumberException   if response is invalid
     * @throws ModbusIOException       if remote slave is unavailable
     * @see CommStatus
     */
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

    /**
     * This function code allows reading the identification and additional information relative to the
     * physical and functional description of a remote device, only.
     * The Read Device Identification interface is modeled as an address space composed of a set
     * of addressable data elements. The data elements are called objects and an object Id
     * identifies them.
     * The interface consists of 3 categories of objects :
     *  Basic Device Identification. All objects of this category are mandatory : VendorName,
     * Product code, and revision number.
     *  Regular Device Identification. In addition to Basic data objects, the device provides
     * additional and optional identification and description data objects. All of the objects of
     * this category are defined in the standard but their implementation is optional .
     *  Extended Device Identification. In addition to regular data objects, the device provides
     * additional and optional identification and description private data about the physical
     * device itself. All of these data are device dependent.
     * ObjectId    ObjectName/Description              Type            M/O         category
     * 0x00        VendorName                          ASCII String    Mandatory   Basic
     * 0x01        ProductCode                         ASCII String    Mandatory   Basic
     * 0x02        MajorMinorRevision                  ASCII String    Mandatory   Basic
     * 0x03        VendorUrl                           ASCII String    Optional    Regular
     * 0x04        ProductName                         ASCII String    Optional    Regular
     * 0x05        ModelName                           ASCII String    Optional    Regular
     * 0x06        UserApplicationName                 ASCII String    Optional    Regular
     * 0x07        Reserved Optional                                   Optional    Regular
     * …
     * 0x7F
     * 0x80        Private objects may be optionally   device          Optional    Extended
     * …           defined. The range [0x80 – 0xFF]   dependant
     * 0xFF        is Product dependant.
     */
    final public MEIReadDeviceIdentification readDeviceIdentification(int serverAddress, int objectId, ReadDeviceIdentificationCode readDeviceId) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        EncapsulatedInterfaceTransportResponse response = (EncapsulatedInterfaceTransportResponse) processRequest(ModbusRequestBuilder.getInstance().buildReadDeviceIdentification(serverAddress, objectId, readDeviceId));
        return (MEIReadDeviceIdentification) response.getMei();
    }

    /* facade */
    @Override
    public void addListener(FrameEventListener listener) {
        getConnection().addListener(listener);
    }

    @Override
    public void removeListener(FrameEventListener listener) {
        getConnection().removeListener(listener);
    }

    @Override
    public void removeListeners() {
        getConnection().removeListeners();
    }

    @Override
    public void fireFrameReceivedEvent(FrameEvent event) {
        getConnection().fireFrameReceivedEvent(event);
    }

    @Override
    public void fireFrameSentEvent(FrameEvent event) {
        getConnection().fireFrameSentEvent(event);
    }

    @Override
    public int countListeners() {
        return getConnection().countListeners();
    }
}
