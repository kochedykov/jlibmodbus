package com.invertor.modbus.data;

import com.invertor.modbus.data.mei.ReadDeviceIdentificationInterface;
import com.invertor.modbus.exception.IllegalDataAddressException;
import com.invertor.modbus.exception.IllegalDataValueException;
import com.invertor.modbus.exception.IllegalFunctionException;
import com.invertor.modbus.msg.base.ModbusFileRecord;
import com.invertor.modbus.utils.ModbusFunctionCode;

import java.util.Map;
import java.util.TreeMap;

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

/*
facade
 */
public class DataHolder {
    final private CommStatus commStatus = new CommStatus();
    final private Map<Integer, FifoQueue> fifoMap = new TreeMap<Integer, FifoQueue>();
    final private Map<Integer, ModbusFile> fileMap = new TreeMap<Integer, ModbusFile>();
    private Coils coils = null;
    private Coils discreteInputs = null;
    private HoldingRegisters holdingRegisters = null;
    private HoldingRegisters inputRegisters = null;
    private SlaveId slaveId = null;
    private ExceptionStatus exceptionStatus = null;
    private ReadDeviceIdentificationInterface readDeviceIdentificationInterface = null;

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

    public int[] readFifoQueue(int fifoPointerAddress) throws IllegalDataValueException, IllegalDataAddressException {
        FifoQueue fifoQueue = fifoMap.get(fifoPointerAddress);
        checkPointer(fifoQueue, fifoPointerAddress);
        return fifoQueue.get();
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
            throw new IllegalFunctionException(ModbusFunctionCode.REPORT_SLAVE_ID.toInt());
        return slaveId.get();
    }

    public int readExceptionStatus() throws IllegalFunctionException {
        if (exceptionStatus == null)
            throw new IllegalFunctionException(ModbusFunctionCode.READ_EXCEPTION_STATUS.toInt());
        return exceptionStatus.get();
    }

    public boolean[] readDiscreteInputRange(int offset, int quantity) throws IllegalDataAddressException, IllegalDataValueException {
        checkPointer(discreteInputs, offset);
        return discreteInputs.getRange(offset, quantity);
    }

    public ModbusFileRecord readFileRecord(ModbusFileRecord fileRecord) throws IllegalDataAddressException, IllegalDataValueException {
        ModbusFile file = getFile(fileRecord.getFileNumber());
        checkPointer(file, fileRecord.getFileNumber());
        fileRecord.writeRegisters(file.read(fileRecord.getRecordNumber(), fileRecord.getRecordLength()));
        return fileRecord;
    }

    public void writeFileRecord(ModbusFileRecord fileRecord) throws IllegalDataAddressException, IllegalDataValueException {
        ModbusFile file = getFile(fileRecord.getFileNumber());
        checkPointer(file, fileRecord.getFileNumber());
        file.write(fileRecord.getRecordNumber(), fileRecord.getRegisters());
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

    public CommStatus getCommStatus() {
        return commStatus;
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

    public FifoQueue getFifoQueue(int fifoPointerAddress) throws IllegalDataAddressException {
        FifoQueue file = fifoMap.get(fifoPointerAddress);
        if (file == null)
            throw new IllegalDataAddressException(fifoPointerAddress);
        return file;
    }

    public void addFifoQueue(FifoQueue fifoQueue, int fifoPointerAddress) throws IllegalDataAddressException {
        if (fifoMap.containsKey(fifoPointerAddress))
            throw new IllegalDataAddressException(fifoPointerAddress);
        fifoMap.put(fifoPointerAddress, fifoQueue);
    }

    public void addFile(ModbusFile file) throws IllegalDataAddressException {
        if (fileMap.containsKey(file.getNumber()))
            throw new IllegalDataAddressException(file.getNumber());
        fileMap.put(file.getNumber(), file);
    }

    public ModbusFile getFile(int number) throws IllegalDataAddressException {
        ModbusFile file = fileMap.get(number);
        if (file == null)
            throw new IllegalDataAddressException(number);
        return file;
    }

    public ReadDeviceIdentificationInterface getReadDeviceIdentificationInterface() {
        return readDeviceIdentificationInterface;
    }

    public void setReadDeviceIdentificationInterface(ReadDeviceIdentificationInterface readDeviceIdentificationInterface) {
        this.readDeviceIdentificationInterface = readDeviceIdentificationInterface;
    }
}
