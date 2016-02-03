package com.sbpinvertor.modbus.data;

import com.sbpinvertor.modbus.exception.IllegalDataAddressException;
import com.sbpinvertor.modbus.exception.IllegalDataValueException;

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

    public int readHoldingRegister(int offset) throws IllegalDataAddressException {
        if (holdingRegisters != null)
            return holdingRegisters.get(offset);
        throw new IllegalDataAddressException(offset);
    }

    public int[] readHoldingRegisterRange(int offset, int quantity) throws IllegalDataAddressException {
        if (holdingRegisters != null)
            return holdingRegisters.getRange(offset, quantity);
        throw new IllegalDataAddressException(offset);
    }

    public void writeHoldingRegister(int offset, int value) throws IllegalDataAddressException, IllegalDataValueException {
        if (holdingRegisters == null)
            throw new IllegalDataAddressException(offset);
        holdingRegisters.set(offset, value);
    }

    public void writeHoldingRegisterRange(int offset, int[] range) throws IllegalDataAddressException, IllegalDataValueException {
        if (holdingRegisters == null)
            throw new IllegalDataAddressException(offset);
        holdingRegisters.setRange(offset, range);
    }

    public int readInputRegister(int offset) throws IllegalDataAddressException {
        if (inputRegisters != null)
            return inputRegisters.get(offset);
        throw new IllegalDataAddressException(offset);
    }

    public int[] readInputRegisterRange(int offset, int quantity) throws IllegalDataAddressException {
        if (inputRegisters != null)
            return inputRegisters.getRange(offset, quantity);
        throw new IllegalDataAddressException(offset);
    }

    public boolean readCoil(int offset) throws IllegalDataAddressException {
        if (coils != null)
            return coils.get(offset);
        throw new IllegalDataAddressException(offset);
    }

    public boolean[] readCoilRange(int offset, int quantity) throws IllegalDataAddressException {
        if (coils != null)
            return coils.getRange(offset, quantity);
        throw new IllegalDataAddressException(offset);
    }

    public void writeCoil(int offset, boolean value) throws IllegalDataAddressException, IllegalDataValueException {
        if (coils == null)
            throw new IllegalDataAddressException(offset);
        coils.set(offset, value);
    }

    public void writeCoilRange(int offset, boolean[] range) throws IllegalDataAddressException, IllegalDataValueException {
        if (coils == null)
            throw new IllegalDataAddressException(offset);
        coils.setRange(offset, range);
    }

    public boolean readDiscreteInput(int offset) throws IllegalDataAddressException {
        if (discreteInputs != null)
            return discreteInputs.get(offset);
        throw new IllegalDataAddressException(offset);
    }

    public boolean[] readDiscreteInputRange(int offset, int quantity) throws IllegalDataAddressException {
        if (discreteInputs != null)
            return discreteInputs.getRange(offset, quantity);
        throw new IllegalDataAddressException(offset);
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
}
