package com.invertor.modbus.master;

import com.invertor.modbus.ModbusMaster;
import com.invertor.modbus.data.CommStatus;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.response.*;

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
abstract public class ModbusMasterSerial extends ModbusMaster {

    final private CommStatus commStatus = new CommStatus();

    @Override
    final synchronized public int readExceptionStatus(int serverAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReadExceptionStatus(serverAddress);
        ReadExceptionStatusResponse response = (ReadExceptionStatusResponse) processRequest(request);
        return response.getExceptionStatus();
    }

    @Override
    final synchronized public byte[] reportSlaveId(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReportSlaveId(serverAddress);
        ReportSlaveIdResponse response = (ReportSlaveIdResponse) processRequest(request);
        return response.getSlaveId();
    }

    @Override
    final synchronized public CommStatus getCommEventCounter(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createGetCommEventCounter(serverAddress);
        GetCommEventCounterResponse response = (GetCommEventCounterResponse) processRequest(request);
        commStatus.setCommStatus(response.getStatus());
        commStatus.setEventCount(response.getEventCount());
        return commStatus;
    }

    @Override
    final synchronized public CommStatus getCommEventLog(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createGetCommEventLog(serverAddress);
        GetCommEventLogResponse response = (GetCommEventLogResponse) processRequest(request);
        commStatus.setCommStatus(response.getStatus());
        commStatus.setEventCount(response.getEventCount());
        commStatus.setBusMessageCount(response.getMessageCount());
        commStatus.setEventQueue(response.getEventQueue());
        return commStatus;
    }

    @Override
    final synchronized public void diagnosticsReturnQueryData(int serverAddress, int queryData) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(requestFactory.createReturnQueryData(serverAddress, queryData));
    }

    @Override
    final synchronized public void diagnosticsRestartCommunicationsOption(int serverAddress, boolean clearLog) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(requestFactory.createRestartCommunicationsOption(serverAddress, clearLog));
    }

    @Override
    final synchronized public int diagnosticsReturnDiagnosticRegister(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = requestFactory.createReturnDiagnosticRegister(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final synchronized public void diagnosticsChangeAsciiInputDelimiter(int serverAddress, int delimiter) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(requestFactory.createChangeAsciiInputDelimiter(serverAddress, delimiter));
    }

    @Override
    final synchronized public void diagnosticsForceListenOnlyMode(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(requestFactory.createForceListenOnlyMode(serverAddress));
    }

    @Override
    final synchronized public void diagnosticsClearCountersAndDiagnosticRegister(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(requestFactory.createClearCountersAndDiagnosticRegister(serverAddress));
    }

    @Override
    final synchronized public int diagnosticsReturnBusMessageCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = requestFactory.createReturnBusMessageCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final synchronized public int diagnosticsReturnBusCommunicationErrorCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = requestFactory.createReturnBusCommunicationErrorCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final synchronized public int diagnosticsReturnBusExceptionErrorCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = requestFactory.createReturnBusExceptionErrorCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final synchronized public int diagnosticsReturnSlaveMessageCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = requestFactory.createReturnSlaveMessageCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final synchronized public int diagnosticsReturnSlaveNoResponseCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = requestFactory.createReturnSlaveNoResponseCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final synchronized public int diagnosticsReturnSlaveNAKCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = requestFactory.createReturnSlaveNAKCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final synchronized public int diagnosticsReturnSlaveBusyCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = requestFactory.createReturnSlaveBusyCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final synchronized public int diagnosticsReturnBusCharacterOverrunCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = requestFactory.createReturnBusCharacterOverrunCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final synchronized public void diagnosticsClearOverrunCounterAndFlag(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(requestFactory.createClearOverrunCounterAndFlag(serverAddress));
    }
}
