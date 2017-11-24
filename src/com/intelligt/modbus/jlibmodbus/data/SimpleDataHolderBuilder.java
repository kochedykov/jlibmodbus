package com.intelligt.modbus.jlibmodbus.data;

import com.intelligt.modbus.jlibmodbus.data.mei.ReadDeviceIdentificationInterface;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataAddressException;

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
        dataHolder.setCoils(new ModbusCoils(coilCount));
    }

    @Override
    public void buildDiscreteInputs() {
        dataHolder.setDiscreteInputs(new ModbusCoils(discreteInputCount));

    }

    @Override
    public void buildHoldingRegisters() {
        dataHolder.setHoldingRegisters(new ModbusHoldingRegisters(holdingRegisterCount));
    }

    @Override
    public void buildInputRegisters() {
        dataHolder.setInputRegisters(new ModbusHoldingRegisters(inputRegisterCount));
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

    @Override
    public void readDeviceIdentificationInterface() {
        dataHolder.setReadDeviceIdentificationInterface(new ReadDeviceIdentificationInterface());
    }
}
