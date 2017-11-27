package com.intelligt.modbus.jlibmodbus.data;

import com.intelligt.modbus.jlibmodbus.data.mei.ReadDeviceIdentificationInterface;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataAddressException;
import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException;
import com.intelligt.modbus.jlibmodbus.exception.IllegalFunctionException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusFileRecord;
import com.intelligt.modbus.jlibmodbus.utils.ModbusFunctionCode;

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
    private ModbusCoils coils = null;
    private ModbusCoils discreteInputs = null;
    private ModbusHoldingRegisters holdingRegisters = null;
    private ModbusHoldingRegisters inputRegisters = null;
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

    public int[] readHoldingRegisterRange(int offset, int quantity) throws IllegalDataAddressException {
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

    public int[] readInputRegisterRange(int offset, int quantity) throws IllegalDataAddressException {
        checkPointer(inputRegisters, offset);
        return inputRegisters.getRange(offset, quantity);
    }

    public int[] readFifoQueue(int fifoPointerAddress) throws IllegalDataValueException, IllegalDataAddressException {
        FifoQueue fifoQueue = fifoMap.get(fifoPointerAddress);
        checkPointer(fifoQueue, fifoPointerAddress);
        return fifoQueue.get();
    }

    public boolean[] readCoilRange(int offset, int quantity) throws IllegalDataAddressException, IllegalDataValueException {
        checkPointer(coils, offset);
        return coils.getRange(offset, quantity);
    }

    public void writeCoil(int offset, boolean value) throws IllegalDataAddressException, IllegalDataValueException {
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

    public void readFileRecord(ModbusFileRecord fileRecord) throws IllegalDataAddressException, IllegalDataValueException {
        ModbusFile file = getFile(fileRecord.getFileNumber());
        checkPointer(file, fileRecord.getFileNumber());
        fileRecord.writeRegisters(file.read(fileRecord.getRecordNumber(), fileRecord.getRecordLength()));
    }

    public void writeFileRecord(ModbusFileRecord fileRecord) throws IllegalDataAddressException, IllegalDataValueException {
        ModbusFile file = getFile(fileRecord.getFileNumber());
        checkPointer(file, fileRecord.getFileNumber());
        file.write(fileRecord.getRecordNumber(), fileRecord.getRegisters());
    }

    public ModbusCoils getCoils() {
        return coils;
    }

    public void setCoils(ModbusCoils coils) {
        this.coils = coils;
    }

    public ModbusCoils getDiscreteInputs() {
        return discreteInputs;
    }

    public void setDiscreteInputs(ModbusCoils discreteInputs) {
        this.discreteInputs = discreteInputs;
    }

    public ModbusHoldingRegisters getHoldingRegisters() {
        return holdingRegisters;
    }

    public void setHoldingRegisters(ModbusHoldingRegisters holdingRegisters) {
        this.holdingRegisters = holdingRegisters;
    }

    public ModbusHoldingRegisters getInputRegisters() {
        return inputRegisters;
    }

    public void setInputRegisters(ModbusHoldingRegisters inputRegisters) {
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
