package com.invertor.modbus.msg.request;

import com.invertor.modbus.data.CommStatus;
import com.invertor.modbus.data.DataHolder;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.msg.response.DiagnosticsResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

/**
 * MODBUS function code 08 provides a series of tests for checking the communication system
 * between a client ( Master) device and a server ( Slave), or for checking various internal error
 * conditions within a server.
 * The function uses a two–byte sub-function code field in the query to define the type of test to
 * be performed. The server echoes both the function code and sub-function code in a normal
 * response. Some of the diagnostics cause data to be returned from the remote device in the
 * data field of a normal response.
 * In general, issuing a diagnostic function to a remote device does not affect the running of the
 * user program in the remote device. User logic, like discrete and registers, is not accessed by
 * the diagnostics. Certain functions can optionally reset error counters in the remote device.
 * A server device can, however, be forced into ‘Listen Only Mode’ in which it will monitor the
 * messages on the communications system but not respond to them. This can affect the
 * outcome of your application program if it depends upon any further exchange of data with the
 * remote device. Generally, the mode is forced to remove a malfunctioning remote device from
 * the communications system.
 * The following diagnostic functions are dedicated to serial line devices.
 * The normal response to the Return Query Data request is to loopback the same data. The
 * function code and sub-function codes are also echoed.
 */
public class DiagnosticsRequest extends ModbusRequest {
    /**
     * Diagnostic uses a two–byte sub-function code field in the query to define the type of test to
     * be performed.
     */
    final static public int CLEAR_LOG = 0xff00;
    private DiagnosticsSubFunctionCode subFunctionCode = DiagnosticsSubFunctionCode.RESERVED;
    private int subFunctionData = 0;

    public DiagnosticsRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getSubFunctionCode().toInt());
        fifo.writeShortBE(getSubFunctionData());
    }

    @Override
    public int requestSize() {
        return 4;
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        DiagnosticsResponse response = new DiagnosticsResponse(getServerAddress());
        response.setSubFunctionCode(getSubFunctionCode());
        CommStatus commStatus = dataHolder.getCommStatus();
        switch (getSubFunctionCode()) {
            case RETURN_QUERY_DATA:
                response.setSubFunctionData(getSubFunctionData());
                break;
            case RESTART_COMMUNICATIONS_OPTION:
                if (getSubFunctionData() == 0xff00) {
                    commStatus.setClearLog(true);
                }
                commStatus.setRestartCommunicationsOption(true);
                response.setSubFunctionData(getSubFunctionData());
                break;
            case RETURN_DIAGNOSTIC_REGISTER:
                response.setSubFunctionData(commStatus.getDiagnosticRegister());
                break;
            case CHANGE_ASCII_INPUT_DELIMITER:
                commStatus.setAsciiInputDelimiter((getSubFunctionData() >> 8) & 0xff);
                response.setSubFunctionData(getSubFunctionData());
                break;
            case FORCE_LISTEN_ONLY_MODE:
                commStatus.setListenOnlyMode(true);
                break;
            case CLEAR_COUNTERS_AND_DIAGNOSTIC_REGISTER:
                commStatus.clearCountersAndDiagnosticRegister();
                response.setSubFunctionData(getSubFunctionData());
                break;
            case RETURN_BUS_MESSAGE_COUNT:
                response.setSubFunctionData(commStatus.getMessageCount());
                break;
            case RETURN_BUS_COMMUNICATION_ERROR_COUNT:
                response.setSubFunctionData(commStatus.getCommunicationErrorCount());
                break;
            case RETURN_BUS_EXCEPTION_ERROR_COUNT:
                response.setSubFunctionData(commStatus.getExceptionErrorCount());
                break;
            case RETURN_SLAVE_MESSAGE_COUNT:
                response.setSubFunctionData(commStatus.getSlaveMessageCount());
                break;
            case RETURN_SLAVE_NO_RESPONSE_COUNT:
                response.setSubFunctionData(commStatus.getSlaveNoResponseCount());
                break;
            case RETURN_SLAVE_NAK_COUNT:
                response.setSubFunctionData(commStatus.getSlaveNAKCount());
                break;
            case RETURN_SLAVE_BUSY_COUNT:
                response.setSubFunctionData(commStatus.getSlaveBusyCount());
                break;
            case RETURN_BUS_CHARACTER_OVERRUN_COUNT:
                response.setSubFunctionData(commStatus.getCharacterOverrunCount());
                break;
            case CLEAR_OVERRUN_COUNTER_AND_FLAG:
                commStatus.setCharacterOverrunCount(0);
                response.setSubFunctionData(getSubFunctionData());
                break;
            case RESERVED:
                break;
        }
        return response;
    }

    @Override
    protected boolean validateResponseImpl(ModbusResponse response) {
        DiagnosticsResponse r = (DiagnosticsResponse) response;

        if (getSubFunctionCode() != r.getSubFunctionCode())
            return false;

        switch (subFunctionCode) {
            case RETURN_QUERY_DATA:
                return getSubFunctionData() == r.getSubFunctionData();
            case RESTART_COMMUNICATIONS_OPTION:
                return getSubFunctionData() == r.getSubFunctionData();
            case RETURN_DIAGNOSTIC_REGISTER:
                break;
            case CHANGE_ASCII_INPUT_DELIMITER:
                return getSubFunctionData() == r.getSubFunctionData();
            case FORCE_LISTEN_ONLY_MODE:
                break;
            case CLEAR_COUNTERS_AND_DIAGNOSTIC_REGISTER:
                return getSubFunctionData() == r.getSubFunctionData();
            case RETURN_BUS_MESSAGE_COUNT:
                break;
            case RETURN_BUS_COMMUNICATION_ERROR_COUNT:
                break;
            case RETURN_BUS_EXCEPTION_ERROR_COUNT:
                break;
            case RETURN_SLAVE_MESSAGE_COUNT:
                break;
            case RETURN_SLAVE_NO_RESPONSE_COUNT:
                break;
            case RETURN_SLAVE_NAK_COUNT:
                break;
            case RETURN_SLAVE_BUSY_COUNT:
                break;
            case RETURN_BUS_CHARACTER_OVERRUN_COUNT:
                break;
            case CLEAR_OVERRUN_COUNTER_AND_FLAG:
                return getSubFunctionData() == r.getSubFunctionData();
            case RESERVED:
                break;
        }
        return true;
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        setSubFunctionCode(DiagnosticsSubFunctionCode.get(fifo.readShortBE()));
        setSubFunctionData(fifo.readShortBE());
    }

    public int getSubFunctionData() {
        return subFunctionData;
    }

    public void setSubFunctionData(int subFunctionData) {
        this.subFunctionData = subFunctionData;
    }

    public DiagnosticsSubFunctionCode getSubFunctionCode() {
        return subFunctionCode;
    }

    public void setSubFunctionCode(DiagnosticsSubFunctionCode subFunctionCode) {
        this.subFunctionCode = subFunctionCode;
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.DIAGNOSTICS.toInt();
    }
}
