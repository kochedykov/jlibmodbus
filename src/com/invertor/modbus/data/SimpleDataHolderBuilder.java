package com.invertor.modbus.data;

import com.invertor.modbus.exception.IllegalDataAddressException;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class SimpleDataHolderBuilder extends DataHolderBuilder {
    final private int coilCount;
    final private int discreteInputCount;
    final private int holdingRegisterCount;
    final private int inputRegisterCount;
    final private int slaveIdSize;

    public SimpleDataHolderBuilder(int size) {
        coilCount = discreteInputCount = holdingRegisterCount = inputRegisterCount = slaveIdSize = size;
    }

    public SimpleDataHolderBuilder(int slaveIdSize, int inputRegisterCount, int holdingRegisterCount, int discreteInputCount, int coilCount) {
        this.slaveIdSize = slaveIdSize;
        this.inputRegisterCount = inputRegisterCount;
        this.holdingRegisterCount = holdingRegisterCount;
        this.discreteInputCount = discreteInputCount;
        this.coilCount = coilCount;
    }

    public DataHolder getDataHolder() {
        return dataHolder;
    }

    @Override
    public void buildCoils() {
        dataHolder.setCoils(new SimpleCoils(coilCount));
    }

    @Override
    public void buildDiscreteInputs() {
        dataHolder.setDiscreteInputs(new SimpleCoils(discreteInputCount));

    }

    @Override
    public void buildHoldingRegisters() {
        dataHolder.setHoldingRegisters(new SimpleHoldingRegisters(holdingRegisterCount));
    }

    @Override
    public void buildInputRegisters() {
        dataHolder.setInputRegisters(new SimpleHoldingRegisters(inputRegisterCount));
    }

    @Override
    public void buildSlaveId() {
        dataHolder.setSlaveId(new SimpleSlaveId(slaveIdSize));
    }

    @Override
    public void buildExceptionStatus() {
        dataHolder.setExceptionStatus(new SimpleExceptionStatus(0));
    }

    @Override
    public void buildFifoQueue() {
        try {
            dataHolder.addFifoQueue(new SimpleFifoQueue(), 0);
        } catch (IllegalDataAddressException e) {
            //it newer be thrown
            e.printStackTrace();
        }
    }
}
