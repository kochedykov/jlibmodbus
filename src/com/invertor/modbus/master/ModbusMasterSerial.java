package com.invertor.modbus.master;

import com.invertor.modbus.ModbusMaster;
import com.invertor.modbus.data.CommStatus;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.base.ModbusRequest;
import com.invertor.modbus.msg.response.GetCommEventCounterResponse;
import com.invertor.modbus.msg.response.GetCommEventLogResponse;
import com.invertor.modbus.msg.response.ReadExceptionStatusResponse;
import com.invertor.modbus.msg.response.ReportSlaveIdResponse;

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
abstract public class ModbusMasterSerial extends ModbusMaster {

    final private CommStatus commStatus = new CommStatus();

    @Override
    public int readExceptionStatus(int serverAddress) throws
            ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReadExceptionStatus(serverAddress);
        ReadExceptionStatusResponse response = (ReadExceptionStatusResponse) processRequest(request);
        return response.getExceptionStatus();
    }

    @Override
    public byte[] reportSlaveId(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createReportSlaveId(serverAddress);
        ReportSlaveIdResponse response = (ReportSlaveIdResponse) processRequest(request);
        return response.getSlaveId();
    }

    @Override
    public CommStatus getCommEventCounter(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createGetCommEventCounter(serverAddress);
        GetCommEventCounterResponse response = (GetCommEventCounterResponse) processRequest(request);
        commStatus.setStatus(response.getStatus());
        commStatus.setEventCount(response.getEventCount());
        return commStatus;
    }

    @Override
    public CommStatus getCommEventLog(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        ModbusRequest request = requestFactory.createGetCommEventLog(serverAddress);
        GetCommEventLogResponse response = (GetCommEventLogResponse) processRequest(request);
        commStatus.setStatus(response.getStatus());
        commStatus.setEventCount(response.getEventCount());
        commStatus.setMessageCount(response.getMessageCount());
        commStatus.setEventQueue(response.getEventQueue());
        return commStatus;
    }
}
