package com.sbpinvertor.modbus.data;

import com.sbpinvertor.modbus.data.base.ModbusRequest;
import com.sbpinvertor.modbus.data.request.*;
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
public class ModbusRequestFactory {

    private ModbusRequestFactory() {

    }

    static public ModbusRequest createReadCoils(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadCoilsRequest(serverAddress, startAddress, quantity);
    }

    static public ModbusRequest createReadDiscreteInputs(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadDiscreteInputsRequest(serverAddress, startAddress, quantity);
    }

    static public ModbusRequest createReadInputRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadInputRegistersRequest(serverAddress, startAddress, quantity);
    }

    static public ModbusRequest createReadHoldingRegisters(int serverAddress, int startAddress, int quantity) throws ModbusNumberException {
        return new ReadHoldingRegistersRequest(serverAddress, startAddress, quantity);
    }

    static public ModbusRequest createWriteSingleCoil(int serverAddress, int startAddress, boolean coil) throws ModbusNumberException {
        return new WriteSingleCoilRequest(serverAddress, startAddress, coil);
    }

    static public ModbusRequest createWriteMultipleCoils(int serverAddress, int startAddress, boolean[] coils) throws ModbusNumberException {
        return new WriteMultipleCoilsRequest(serverAddress, startAddress, coils);
    }

    static public ModbusRequest createWriteMultipleRegisters(int serverAddress, int startAddress, int[] registers) throws ModbusNumberException {
        return new WriteMultipleRegistersRequest(serverAddress, startAddress, registers);
    }

    static public ModbusRequest createWriteSingleRegister(int serverAddress, int startAddress, int register) throws ModbusNumberException {
        return new WriteSingleRegisterRequest(serverAddress, startAddress, register);
    }
}
