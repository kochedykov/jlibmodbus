package com.invertor.modbus.serial;

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
public class SerialParameters {
    private String device = null;
    private SerialPort.BaudRate baudRate;
    private int dataBits;
    private int stopBits;
    private SerialPort.Parity parity;

    public SerialParameters() {
        String[] devices = SerialUtils.getPortList();
        if (devices.length > 0) {
            setDevice(devices[0]);
        }
        setBaudRate(SerialPort.BaudRate.BAUD_RATE_115200);
        setDataBits(8);
        setStopBits(1);
        setParity(SerialPort.Parity.NONE);
    }

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
