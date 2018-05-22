package com.intelligt.modbus.jlibmodbus.master;

import com.intelligt.modbus.jlibmodbus.data.CommStatus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.msg.ModbusRequestBuilder;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.*;
import com.intelligt.modbus.jlibmodbus.net.ModbusConnection;

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

    public ModbusMasterSerial(ModbusConnection conn) {
        super(conn);
    }

    @Override
    final public int readExceptionStatus(int serverAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReadExceptionStatus(serverAddress);
        ReadExceptionStatusResponse response = (ReadExceptionStatusResponse) processRequest(request);
        return response.getExceptionStatus();
    }

    @Override
    final public byte[] reportSlaveId(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReportSlaveId(serverAddress);
        ReportSlaveIdResponse response = (ReportSlaveIdResponse) processRequest(request);
        return response.getSlaveId();
    }

    @Override
    final public CommStatus getCommEventCounter(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildGetCommEventCounter(serverAddress);
        GetCommEventCounterResponse response = (GetCommEventCounterResponse) processRequest(request);
        synchronized (commStatus) {
            commStatus.setCommStatus(response.getStatus());
            commStatus.setEventCount(response.getEventCount());
        }
        return commStatus;
    }

    @Override
    final public CommStatus getCommEventLog(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildGetCommEventLog(serverAddress);
        GetCommEventLogResponse response = (GetCommEventLogResponse) processRequest(request);
        synchronized (commStatus) {
            commStatus.setCommStatus(response.getStatus());
            commStatus.setEventCount(response.getEventCount());
            commStatus.setBusMessageCount(response.getMessageCount());
            commStatus.setEventQueue(response.getEventQueue());
        }
        return commStatus;
    }

    @Override
    final public void diagnosticsReturnQueryData(int serverAddress, int queryData) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildReturnQueryData(serverAddress, queryData));
    }

    @Override
    final public void diagnosticsRestartCommunicationsOption(int serverAddress, boolean clearLog) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildRestartCommunicationsOption(serverAddress, clearLog));
    }

    @Override
    final public int diagnosticsReturnDiagnosticRegister(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReturnDiagnosticRegister(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final public void diagnosticsChangeAsciiInputDelimiter(int serverAddress, int delimiter) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildChangeAsciiInputDelimiter(serverAddress, delimiter));
    }

    @Override
    final public void diagnosticsForceListenOnlyMode(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildForceListenOnlyMode(serverAddress));
    }

    @Override
    final public void diagnosticsClearCountersAndDiagnosticRegister(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildClearCountersAndDiagnosticRegister(serverAddress));
    }

    @Override
    final public int diagnosticsReturnBusMessageCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReturnBusMessageCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final public int diagnosticsReturnBusCommunicationErrorCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReturnBusCommunicationErrorCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final public int diagnosticsReturnBusExceptionErrorCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReturnBusExceptionErrorCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final public int diagnosticsReturnSlaveMessageCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReturnSlaveMessageCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final public int diagnosticsReturnSlaveNoResponseCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReturnSlaveNoResponseCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final public int diagnosticsReturnSlaveNAKCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReturnSlaveNAKCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final public int diagnosticsReturnSlaveBusyCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReturnSlaveBusyCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final public int diagnosticsReturnBusCharacterOverrunCount(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        ModbusRequest request = ModbusRequestBuilder.getInstance().buildReturnBusCharacterOverrunCount(serverAddress);
        DiagnosticsResponse response = (DiagnosticsResponse) processRequest(request);
        return response.getSubFunctionData();
    }

    @Override
    final public void diagnosticsClearOverrunCounterAndFlag(int serverAddress) throws ModbusNumberException, ModbusProtocolException, ModbusIOException {
        processRequest(ModbusRequestBuilder.getInstance().buildClearOverrunCounterAndFlag(serverAddress));
    }
}
