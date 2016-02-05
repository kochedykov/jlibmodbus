package com.sbpinvertor.modbus.data;

import com.sbpinvertor.modbus.exception.IllegalDataAddressException;
import com.sbpinvertor.modbus.exception.IllegalDataValueException;
import com.sbpinvertor.modbus.exception.IllegalFunctionException;
import com.sbpinvertor.modbus.utils.ModbusFunctionCode;

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

/*
facade
 */
public class DataHolder {

    private Coils coils = null;
    private Coils discreteInputs = null;
    private HoldingRegisters holdingRegisters = null;
    private HoldingRegisters inputRegisters = null;
    private SlaveId slaveId = null;
    private ExceptionStatus exceptionStatus = null;

    private void checkPointer(Object o, int offset) throws IllegalDataAddressException {
        if (o == null)
            throw new IllegalDataAddressException(offset);
    }

    public int readHoldingRegister(int offset) throws IllegalDataAddressException {
        checkPointer(holdingRegisters, offset);
        return holdingRegisters.get(offset);
    }

    public int[] readHoldingRegisterRange(int offset, int quantity) throws IllegalDataAddressException, IllegalDataValueException {
        checkPointer(holdingRegisters, offset);
        return holdingRegisters.getRange(offset, quantity);
    }

    public void writeHoldingRegister(int offset, int value) throws IllegalDataAddressException, IllegalDataValueException {
        checkPointer(holdingRegisters, offset);
        holdingRegisters.set(offset, value);
    }

    public void writeHoldingRegisterRange(int offset, int[] range) throws IllegalDataAddressException, IllegalDataValueException {
        checkPointer(holdingRegisters, offset);
        holdingRegisters.setRange(offset, range);
    }

    public int[] readInputRegisterRange(int offset, int quantity) throws IllegalDataAddressException, IllegalDataValueException {
        checkPointer(inputRegisters, offset);
        return inputRegisters.getRange(offset, quantity);
    }

    public boolean[] readCoilRange(int offset, int quantity) throws IllegalDataAddressException, IllegalDataValueException {
        checkPointer(holdingRegisters, offset);
        return coils.getRange(offset, quantity);
    }

    public void writeCoil(int offset, boolean value) throws IllegalDataAddressException {
        checkPointer(coils, offset);
        coils.set(offset, value);
    }

    public void writeCoilRange(int offset, boolean[] range) throws IllegalDataAddressException, IllegalDataValueException {
        checkPointer(coils, offset);
        coils.setRange(offset, range);
    }

    public byte[] readSlaveId() throws IllegalFunctionException {
        if (slaveId == null)
            throw new IllegalFunctionException(ModbusFunctionCode.REPORT_SLAVE_ID);
        return slaveId.get();
    }

    public int readExceptionStatus() throws IllegalFunctionException {
        if (exceptionStatus == null)
            throw new IllegalFunctionException(ModbusFunctionCode.READ_EXCEPTION_STATUS);
        return exceptionStatus.get();
    }

    public boolean[] readDiscreteInputRange(int offset, int quantity) throws IllegalDataAddressException, IllegalDataValueException {
        checkPointer(discreteInputs, offset);
        return discreteInputs.getRange(offset, quantity);
    }

    public Coils getCoils() {
        return coils;
    }

    public void setCoils(Coils coils) {
        this.coils = coils;
    }

    public Coils getDiscreteInputs() {
        return discreteInputs;
    }

    public void setDiscreteInputs(Coils discreteInputs) {
        this.discreteInputs = discreteInputs;
    }

    public HoldingRegisters getHoldingRegisters() {
        return holdingRegisters;
    }

    public void setHoldingRegisters(HoldingRegisters holdingRegisters) {
        this.holdingRegisters = holdingRegisters;
    }

    public HoldingRegisters getInputRegisters() {
        return inputRegisters;
    }

    public void setInputRegisters(HoldingRegisters inputRegisters) {
        this.inputRegisters = inputRegisters;
    }

    public SlaveId getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(SlaveId slaveId) {
        this.slaveId = slaveId;
    }

    public ExceptionStatus getExceptionStatus() {
        return exceptionStatus;
    }

    public void setExceptionStatus(ExceptionStatus exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }
}
