package com.sbpinvertor.modbus.data;

import com.sbpinvertor.modbus.ModbusFunction;
import com.sbpinvertor.modbus.data.base.ModbusResponse;
import com.sbpinvertor.modbus.data.response.ReadHoldingRegistersResponse;
import com.sbpinvertor.modbus.exception.ModbusDataException;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.utils.ByteFifo;

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
public class ModbusResponseFactory {

    public static ModbusResponse getResponse(ByteFifo fifo) throws ModbusDataException, ModbusTransportException {
        ModbusResponse response = null;
        try {
            int serverAddress = fifo.read();
            int functionCode = fifo.read();
            switch (ModbusFunction.getFunction(functionCode)) {
                case READ_COILS:
                case READ_DISCRETE_INPUTS:
                case READ_HOLDING_REGISTERS:
                    response = new ReadHoldingRegistersResponse(serverAddress);
                    break;
                case READ_INPUT_REGISTERS:
                case WRITE_SINGLE_COIL:
                case WRITE_SINGLE_REGISTER:
                case WRITE_MULTIPLE_COILS:
                case WRITE_MULTIPLE_REGISTERS:
                case REPORT_SLAVE_ID:
                case READ_FILE_RECORD:
                case WRITE_FILE_RECORD:
                    break;
            }
            if (response != null) {
                if (ModbusFunction.isException(functionCode)) {
                    response.setException();
                }
                response.read(fifo);
                if (response.isException()) {
                    throw new ModbusTransportException(response.getException());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
