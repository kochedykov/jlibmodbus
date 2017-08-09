package com.invertor.modbus.msg;

import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.AbstractDataRequest;
import com.invertor.modbus.msg.base.AbstractMultipleRequest;
import com.invertor.modbus.msg.base.ModbusFileRecord;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.base.mei.ReadDeviceIdentificationCode;
import com.invertor.modbus.msg.request.*;
import com.invertor.modbus.utils.DiagnosticsSubFunctionCode;

import java.util.Arrays;

/**
 * Created by kochedykov on 09.08.2017.
 */
public class ModbusRequestBuilder {

    static public ModbusRequestBuilder getInstance() {
        return SingletonHolder.instance;
    }

    private ModbusRequest setBaseParameter(ModbusRequest request, int serverAddress) throws ModbusNumberException {
        request.setServerAddress(serverAddress);
        return request;
    }

    private void setSimpleDataRequestParameters(AbstractDataRequest request, int serverAddress, int startAddress) throws ModbusNumberException {
        request.setServerAddress(serverAddress);
        request.setStartAddress(startAddress);
    }

    private void setMultipleDataRequestParameters(AbstractMultipleRequest request, int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        setSimpleDataRequestParameters(request, serverAddress, startAddress);
        request.setQuantity(quantity);
    }

    public ModbusRequest buildReadCoils(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        ReadCoilsRequest request = new ReadCoilsRequest();
        setMultipleDataRequestParameters(request, serverAddress, startAddress, quantity);
        return request;
    }

    public ModbusRequest buildReadDiscreteInputs(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest();
        setMultipleDataRequestParameters(request, serverAddress, startAddress, quantity);
        return request;
    }

    public ModbusRequest buildReadInputRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        ReadInputRegistersRequest request = new ReadInputRegistersRequest();
        setMultipleDataRequestParameters(request, serverAddress, startAddress, quantity);
        return request;
    }

    public ModbusRequest buildReadHoldingRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest();
        setMultipleDataRequestParameters(request, serverAddress, startAddress, quantity);
        return request;
    }

    public ModbusRequest buildReadWriteMultipleRegisters(int serverAddress, int readAddress, int readQuantity, int writeAddress, int[] registers) throws ModbusNumberException {
        ReadWriteMultipleRegistersRequest request = new ReadWriteMultipleRegistersRequest();
        request.setServerAddress(serverAddress);
        request.setReadAddress(readAddress);
        request.setReadQuantity(readQuantity);
        request.setWriteAddress(writeAddress);
        request.setWriteRegisters(registers);
        return request;
    }

    public ModbusRequest buildWriteSingleCoil(int serverAddress, int startAddress, boolean coil) throws ModbusNumberException {
        WriteSingleCoilRequest request = new WriteSingleCoilRequest();
        setSimpleDataRequestParameters(request, serverAddress, startAddress);
        request.setCoil(coil);
        return request;
    }

    public ModbusRequest buildWriteMultipleCoils(int serverAddress, int startAddress, boolean[] coils) throws ModbusNumberException {
        WriteMultipleCoilsRequest request = new WriteMultipleCoilsRequest();
        setSimpleDataRequestParameters(request, serverAddress, startAddress);
        request.setCoils(coils);

        return request;
    }

    public ModbusRequest buildWriteMultipleRegisters(int serverAddress, int startAddress, int[] registers) throws ModbusNumberException {
        WriteMultipleRegistersRequest request = new WriteMultipleRegistersRequest();
        setSimpleDataRequestParameters(request, serverAddress, startAddress);
        request.setRegisters(registers);
        return request;
    }

    public ModbusRequest buildWriteMultipleRegisters(int serverAddress, int startAddress, byte[] bytes) throws ModbusNumberException {
        WriteMultipleRegistersRequest request = new WriteMultipleRegistersRequest();
        setSimpleDataRequestParameters(request, serverAddress, startAddress);
        request.setValues(bytes);
        return request;
    }

    public ModbusRequest buildWriteSingleRegister(int serverAddress, int startAddress, int register) throws ModbusNumberException {
        WriteSingleRegisterRequest request = new WriteSingleRegisterRequest();
        setSimpleDataRequestParameters(request, serverAddress, startAddress);
        request.setValue(register);
        return request;
    }

    public ModbusRequest buildMaskWriteRegister(int serverAddress, int startAddress, int and, int or) throws ModbusNumberException {
        MaskWriteRegisterRequest request = new MaskWriteRegisterRequest();
        setSimpleDataRequestParameters(request, serverAddress, startAddress);
        request.setMaskAnd(and);
        request.setMaskOr(or);
        return request;
    }

    public ModbusRequest buildReadExceptionStatus(int serverAddress) throws ModbusNumberException {
        return setBaseParameter(new ReadExceptionStatusRequest(), serverAddress);
    }

    public ModbusRequest buildReportSlaveId(int serverAddress) throws ModbusNumberException {
        return setBaseParameter(new ReportSlaveIdRequest(), serverAddress);
    }

    public ModbusRequest buildGetCommEventCounter(int serverAddress) throws ModbusNumberException {
        return setBaseParameter(new GetCommEventCounterRequest(), serverAddress);
    }

    public ModbusRequest buildGetCommEventLog(int serverAddress) throws ModbusNumberException {
        return setBaseParameter(new GetCommEventLogRequest(), serverAddress);
    }

    public ModbusRequest buildReadFifoQueue(int serverAddress, int fifoPointerAddress) throws ModbusNumberException {
        ReadFifoQueueRequest request = new ReadFifoQueueRequest();
        request.setServerAddress(serverAddress);
        request.setStartAddress(fifoPointerAddress);
        return request;
    }

    public ModbusRequest buildReadFileRecord(int serverAddress, ModbusFileRecord[] records) throws ModbusNumberException {
        ReadFileRecordRequest request = new ReadFileRecordRequest();
        request.setServerAddress(serverAddress);
        request.addFileRecords(Arrays.asList(records));
        return request;
    }

    public ModbusRequest buildWriteFileRecord(int serverAddress, ModbusFileRecord record) throws ModbusNumberException {
        WriteFileRecordRequest request = new WriteFileRecordRequest();
        request.setServerAddress(serverAddress);
        request.setFileRecord(record);
        return request;
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
     * @see com.invertor.modbus.msg.request.DiagnosticsRequest
     * @see com.invertor.modbus.utils.DiagnosticsSubFunctionCode
     */
    public ModbusRequest buildDiagnostics(DiagnosticsSubFunctionCode subFunctionCode, int serverAddress, int data) throws ModbusNumberException {
        DiagnosticsRequest request = new DiagnosticsRequest();
        request.setServerAddress(serverAddress);
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
    public ModbusRequest buildReturnQueryData(int serverAddress, int queryData) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RETURN_QUERY_DATA, serverAddress, queryData);
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
    public ModbusRequest buildRestartCommunicationsOption(int serverAddress, boolean clearLog) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RESTART_COMMUNICATIONS_OPTION, serverAddress, clearLog ? DiagnosticsRequest.CLEAR_LOG : 0);
    }

    /**
     * Returns the contents of the remote device’s 16–bit diagnostic register are returned in the response.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest buildReturnDiagnosticRegister(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RETURN_DIAGNOSTIC_REGISTER, serverAddress, 0);
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
    public ModbusRequest buildChangeAsciiInputDelimiter(int serverAddress, int delimiter) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.CHANGE_ASCII_INPUT_DELIMITER, serverAddress, delimiter);
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
    public ModbusRequest buildForceListenOnlyMode(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.FORCE_LISTEN_ONLY_MODE, serverAddress, 0);
    }

    /**
     * The goal is to clear all counters and the diagnostic register. Counters are also cleared upon power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest buildClearCountersAndDiagnosticRegister(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.CLEAR_COUNTERS_AND_DIAGNOSTIC_REGISTER, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of messages that the remote device has detected
     * on the communications system since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest buildReturnBusMessageCount(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RETURN_BUS_MESSAGE_COUNT, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of CRC errors encountered by the remote device
     * since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest buildReturnBusCommunicationErrorCount(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RETURN_BUS_COMMUNICATION_ERROR_COUNT, serverAddress, 0);
    }

    /**
     * The response data field returns the quantity of MODBUS exception responses returned by the
     * remote device since its last restart, clear counters operation, or power–up.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest buildReturnBusExceptionErrorCount(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RETURN_BUS_EXCEPTION_ERROR_COUNT, serverAddress, 0);
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
    public ModbusRequest buildReturnSlaveMessageCount(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RETURN_SLAVE_MESSAGE_COUNT, serverAddress, 0);
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
    public ModbusRequest buildReturnSlaveNoResponseCount(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RETURN_SLAVE_NO_RESPONSE_COUNT, serverAddress, 0);
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
    public ModbusRequest buildReturnSlaveNAKCount(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RETURN_SLAVE_NAK_COUNT, serverAddress, 0);
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
    public ModbusRequest buildReturnSlaveBusyCount(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RETURN_SLAVE_BUSY_COUNT, serverAddress, 0);
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
    public ModbusRequest buildReturnBusCharacterOverrunCount(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.RETURN_BUS_CHARACTER_OVERRUN_COUNT, serverAddress, 0);
    }

    /**
     * Clears the overrun error counter and reset the error flag.
     *
     * @param serverAddress a slave address
     * @return DiagnosticsRequest instance
     * @throws ModbusNumberException if server address is in-valid
     */
    public ModbusRequest buildClearOverrunCounterAndFlag(int serverAddress) throws ModbusNumberException {
        return buildDiagnostics(DiagnosticsSubFunctionCode.CLEAR_OVERRUN_COUNTER_AND_FLAG, serverAddress, 0);
    }

    public ModbusRequest buildReadDeviceIdentification(int serverAddress, int objectId, ReadDeviceIdentificationCode readDeviceId) throws ModbusNumberException {
        ReadDeviceIdentificationRequest request = new ReadDeviceIdentificationRequest();
        request.setServerAddress(serverAddress);
        request.setObjectId(objectId);
        request.setReadDeviceId(readDeviceId);
        return request;
    }

    static private class SingletonHolder {
        final static private ModbusRequestBuilder instance = new ModbusRequestBuilder();
    }
}
