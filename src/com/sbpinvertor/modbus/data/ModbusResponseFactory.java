package com.sbpinvertor.modbus.data;

import com.sbpinvertor.modbus.data.base.ModbusResponse;
import com.sbpinvertor.modbus.data.response.*;
import com.sbpinvertor.modbus.exception.ModbusNumberException;

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
final public class ModbusResponseFactory {

    private ModbusResponseFactory() {

    }

    static public ModbusResponse createReadCoils(int serverAddress, boolean[] coils) throws ModbusNumberException {
        return new ReadCoilsResponse(serverAddress, coils);
    }

    static public ModbusResponse createReadDiscreteInputs(int serverAddress, boolean[] discreteInputs) throws ModbusNumberException {
        return new ReadDiscreteInputsResponse(serverAddress, discreteInputs);
    }

    static public ModbusResponse createReadInputRegisters(int serverAddress, int[] inputRegisters) throws ModbusNumberException {
        return new ReadInputRegistersResponse(serverAddress, inputRegisters);
    }

    static public ModbusResponse createReadHoldingRegisters(int serverAddress, int[] holdingRegisters) throws ModbusNumberException {
        return new ReadHoldingRegistersResponse(serverAddress, holdingRegisters);
    }

    static public ModbusResponse createWriteSingleCoil(int serverAddress, int startAddress, boolean coil) throws ModbusNumberException {
        return new WriteSingleCoilResponse(serverAddress, startAddress, coil);
    }

    static public ModbusResponse createWriteMultipleCoils(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new WriteMultipleCoilsResponse(serverAddress, startAddress, quantity);
    }

    static public ModbusResponse createWriteMultipleRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new WriteMultipleRegistersResponse(serverAddress, startAddress, quantity);
    }

    static public ModbusResponse createWriteSingleRegister(int serverAddress, int startAddress, int register) throws ModbusNumberException {
        return new WriteSingleRegisterResponse(serverAddress, startAddress, register);
    }
}
