package com.intelligt.modbus.jlibmodbus.serial;

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
public class SerialParameters {
    private String device = null;
    private SerialPort.BaudRate baudRate;
    private int dataBits;
    private int stopBits;
    private SerialPort.Parity parity;

    public SerialParameters() {
        setBaudRate(SerialPort.BaudRate.BAUD_RATE_115200);
        setDataBits(8);
        setStopBits(1);
        setParity(SerialPort.Parity.NONE);
    }

    /**
     * @param device   the name(path) of the serial port
     * @param baudRate baud rate
     * @param dataBits the number of data bits
     * @param stopBits the number of stop bits(1,2)
     * @param parity   parity check (NONE, EVEN, ODD, MARK, SPACE)
     */
    public SerialParameters(String device, SerialPort.BaudRate baudRate, int dataBits, int stopBits, SerialPort.Parity parity) {
        setDevice(device);
        setBaudRate(baudRate);
        setDataBits(dataBits);
        setStopBits(stopBits);
        setParity(parity);
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getBaudRate() {
        return baudRate.getValue();
    }

    public void setBaudRate(SerialPort.BaudRate baudRate) {
        this.baudRate = baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public SerialPort.Parity getParity() {
        return parity;
    }

    public void setParity(SerialPort.Parity parity) {
        this.parity = parity;
    }
}
