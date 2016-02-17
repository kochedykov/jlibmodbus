package com.invertor.modbus.msg;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.ModbusFileRecord;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.request.*;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.utils.DiagnosticsSubFunctionCode;
import com.invertor.modbus.utils.ModbusFunctionCode;

import java.io.IOException;

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
final public class ModbusRequestFactory implements ModbusMessageFactory {

    private ModbusRequestFactory() {

    }

    static public ModbusRequestFactory getInstance() {
        return SingletonHolder.instance;
    }

    public ModbusRequest createReadCoils(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadCoilsRequest(serverAddress, startAddress, quantity);
    }

    public ModbusRequest createReadDiscreteInputs(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadDiscreteInputsRequest(serverAddress, startAddress, quantity);
    }

    public ModbusRequest createReadInputRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadInputRegistersRequest(serverAddress, startAddress, quantity);
    }

    public ModbusRequest createReadHoldingRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadHoldingRegistersRequest(serverAddress, startAddress, quantity);
    }

    public ModbusRequest createReadWriteMultipleRegisters(int serverAddress, int readAddress, int readQuantity, int writeAddress, int[] registers) throws ModbusNumberException {
        return new ReadWriteMultipleRegistersRequest(serverAddress, readAddress, readQuantity, writeAddress, registers);
    }

    public ModbusRequest createWriteSingleCoil(int serverAddress, int startAddress, boolean coil) throws ModbusNumberException {
        return new WriteSingleCoilRequest(serverAddress, startAddress, coil);
    }

    public ModbusRequest createWriteMultipleCoils(int serverAddress, int startAddress, boolean[] coils) throws ModbusNumberException {
        return new WriteMultipleCoilsRequest(serverAddress, startAddress, coils);
    }

    public ModbusRequest createWriteMultipleRegisters(int serverAddress, int startAddress, int[] registers) throws ModbusNumberException {
        return new WriteMultipleRegistersRequest(serverAddress, startAddress, registers);
    }

    public ModbusRequest createWriteSingleRegister(int serverAddress, int startAddress, int register) throws ModbusNumberException {
        return new WriteSingleRegisterRequest(serverAddress, startAddress, register);
    }

    public ModbusRequest createMaskWriteRegister(int serverAddress, int startAddress, int and, int or) throws ModbusNumberException {
        return new MaskWriteRegisterRequest(serverAddress, startAddress, and, or);
    }

    public ModbusRequest createReadExceptionStatus(int serverAddress) throws ModbusNumberException {
        return new ReadExceptionStatusRequest(serverAddress);
    }

    public ModbusRequest createReportSlaveId(int serverAddress) throws ModbusNumberException {
        return new ReportSlaveIdRequest(serverAddress);
    }

    public ModbusRequest createGetCommEventCounter(int serverAddress) throws ModbusNumberException {
        return new GetCommEventCounterRequest(serverAddress);
    }

    public ModbusRequest createGetCommEventLog(int serverAddress) throws ModbusNumberException {
        return new GetCommEventLogRequest(serverAddress);
    }

    public ModbusRequest createReadFifoQueue(int serverAddress, int fifoPointerAddress) throws ModbusNumberException {
        return new ReadFifoQueueRequest(serverAddress, fifoPointerAddress);
    }

    public ModbusRequest createReadFileRecord(int serverAddress, ModbusFileRecord[] records) throws ModbusNumberException {
        return new ReadFileRecordRequest(serverAddress, records);
    }

    public ModbusRequest createWriteFileRecord(int serverAddress, ModbusFileRecord record) throws ModbusNumberException {
        return new WriteFileRecordRequest(serverAddress, record);
    }

    /**
     * The function uses a sub-function code field in the query to define the type of test to
     * be performed. The server echoes both the function code and sub-function code in a normal
     * response. Some of the diagnostics cause data to be returned from the remote device in the
     * data field of a normal response.
     *
     * @param subFunctionCode a sub-function code
     * @param serverAddress   a slave address
     * @param data            request data field
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     * @see DiagnosticsRequest
     * @see DiagnosticsSubFunctionCode
     */
    public ModbusRequest createDiagnostics(DiagnosticsSubFunctionCode subFunctionCode, int serverAddress, int data) throws ModbusNumberException {
        DiagnosticsRequest request = new DiagnosticsRequest(serverAddress);
        request.setSubFunctionCode(subFunctionCode);
        request.setSubFunctionData(data);
        return request;
    }

    /**
     * The data passed in the request data field is to be returned (looped back) in the response. The
     * entire response message should be identical to the request.
     *
     * @param serverAddress a slave address
     * @param queryData     request data field
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createReturnQueryData(int serverAddress, int queryData) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RETURN_QUERY_DATA, serverAddress, queryData);
    }

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
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createRestartCommunicationsOption(int serverAddress, boolean clearLog) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RESTART_COMMUNICATIONS_OPTION, serverAddress, clearLog ? DiagnosticsRequest.CLEAR_LOG : 0);
    }

    /**
     * Returns the contents of the remote device’s 16–bit diagnostic register are returned in the response.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createReturnDiagnosticRegister(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RETURN_DIAGNOSTIC_REGISTER, serverAddress, 0);
    }

    /**
     * The character passed in the request data field becomes the end of message delimiter
     * for future messages (replacing the default LF character). This function is useful in cases of a
     * Line Feed is not required at the end of ASCII messages.
     *
     * @param serverAddress a slave address
     * @param delimiter     request data field
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createChangeAsciiInputDelimiter(int serverAddress, int delimiter) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.CHANGE_ASCII_INPUT_DELIMITER, serverAddress, delimiter);
    }

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
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createForceListenOnlyMode(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.FORCE_LISTEN_ONLY_MODE, serverAddress, 0);
    }

    /**
     * The goal is to clear all counters and the diagnostic register. Counters are also cleared upon power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createClearCountersAndDiagnosticRegister(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.CLEAR_COUNTERS_AND_DIAGNOSTIC_REGISTER, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of messages that the remote device has detected
     * on the communications system since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createReturnBusMessageCount(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RETURN_BUS_MESSAGE_COUNT, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of CRC errors encountered by the remote device
     * since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createReturnBusCommunicationErrorCount(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RETURN_BUS_COMMUNICATION_ERROR_COUNT, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of MODBUS exception responses returned by the
     * remote device since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createReturnBusExceptionErrorCount(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RETURN_BUS_EXCEPTION_ERROR_COUNT, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of messages addressed to the remote device, or
     * broadcast, that the remote device has processed since its last restart, clear counters
     * operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createReturnSlaveMessageCount(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RETURN_SLAVE_MESSAGE_COUNT, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of messages addressed to the remote device for
     * which it has returned no response (neither a normal response nor an exception response),
     * since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createReturnSlaveNoResponseCount(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RETURN_SLAVE_NO_RESPONSE_COUNT, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of messages addressed to the remote device for
     * which it returned a Negative Acknowledge (NAK) exception response, since its last restart,
     * clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createReturnSlaveNAKCount(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RETURN_SLAVE_NAK_COUNT, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of messages addressed to the remote device for
     * which it returned a Slave Device Busy exception response, since its last restart, clear
     * counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createReturnSlaveBusyCount(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RETURN_SLAVE_BUSY_COUNT, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of messages addressed to the remote device that
     * it could not handle due to a character overrun condition, since its last restart, clear counters
     * operation, or power–up. A character overrun is caused by data characters arriving at the port
     * faster than they can be stored, or by the loss of a character due to a hardware malfunction.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createReturnBusCharacterOverrunCount(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.RETURN_BUS_CHARACTER_OVERRUN_COUNT, serverAddress, 0);
    }

    /**
     * Clears the overrun error counter and reset the error flag.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest createClearOverrunCounterAndFlag(int serverAddress) throws ModbusNumberException {
        return createDiagnostics(DiagnosticsSubFunctionCode.CLEAR_OVERRUN_COUNTER_AND_FLAG, serverAddress, 0);
    }

    @Override
    public ModbusMessage createMessage(ModbusInputStream fifo) throws ModbusNumberException, ModbusIOException {
        ModbusMessage msg;
        int serverAddress;
        int functionCode;
        try {
            serverAddress = fifo.read();
            functionCode = fifo.read();
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
        switch (ModbusFunctionCode.getFunctionCode(functionCode)) {
            case READ_COILS:
                msg = new ReadCoilsRequest(serverAddress);
                break;
            case READ_DISCRETE_INPUTS:
                msg = new ReadDiscreteInputsRequest(serverAddress);
                break;
            case READ_HOLDING_REGISTERS:
                msg = new ReadHoldingRegistersRequest(serverAddress);
                break;
            case READ_INPUT_REGISTERS:
                msg = new ReadInputRegistersRequest(serverAddress);
                break;
            case WRITE_SINGLE_COIL:
                msg = new WriteSingleCoilRequest(serverAddress);
                break;
            case WRITE_SINGLE_REGISTER:
                msg = new WriteSingleRegisterRequest(serverAddress);
                break;
            case WRITE_MULTIPLE_COILS:
                msg = new WriteMultipleCoilsRequest(serverAddress);
                break;
            case WRITE_MULTIPLE_REGISTERS:
                msg = new WriteMultipleRegistersRequest(serverAddress);
                break;
            case MASK_WRITE_REGISTER:
                msg = new MaskWriteRegisterRequest(serverAddress);
                break;
            case READ_WRITE_MULTIPLE_REGISTERS:
                msg = new ReadWriteMultipleRegistersRequest(serverAddress);
                break;
            case READ_FIFO_QUEUE:
                msg = new ReadFifoQueueRequest(serverAddress);
                break;
            case READ_FILE_RECORD:
                msg = new ReadFileRecordRequest(serverAddress);
                break;
            case WRITE_FILE_RECORD:
                msg = new WriteFileRecordRequest(serverAddress);
                break;
            case READ_EXCEPTION_STATUS:
                msg = new ReadExceptionStatusRequest(serverAddress);
                break;
            case REPORT_SLAVE_ID:
                msg = new ReportSlaveIdRequest(serverAddress);
                break;
            case GET_COMM_EVENT_COUNTER:
                msg = new GetCommEventCounterRequest(serverAddress);
                break;
            case GET_COMM_EVENT_LOG:
                msg = new GetCommEventLogRequest(serverAddress);
                break;
            case DIAGNOSTICS:
                msg = new DiagnosticsRequest(serverAddress);
                break;
            case ENCAPSULATED_INTERFACE_TRANSPORT:
            default:
                msg = new IllegalFunctionRequest(serverAddress, functionCode);
        }
        msg.read(fifo);
        return msg;
    }

    static private class SingletonHolder {
        final static private ModbusRequestFactory instance = new ModbusRequestFactory();
    }
}
