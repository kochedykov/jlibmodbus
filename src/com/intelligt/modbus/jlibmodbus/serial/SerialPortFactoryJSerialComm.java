package com.intelligt.modbus.jlibmodbus.serial;

import com.intelligt.modbus.jlibmodbus.Modbus;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (C) 2017 Vladislav Y. Kochedykov
 *
 * [http://jlibmodbus.sourceforge.net]
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
public class SerialPortFactoryJSerialComm implements SerialPortAbstractFactory {
    public SerialPort createSerial(SerialParameters sp) throws SerialPortException {
        try {
            Class.forName("com.fazecast.jSerialComm.SerialPort");
        } catch (ClassNotFoundException e) {
            throw new SerialPortException(e);
        }
        return new SerialPortJSerialComm(sp);
    }

    @Override
    public List<String> getPortIdentifiers() {

        com.fazecast.jSerialComm.SerialPort[] ports = com.fazecast.jSerialComm.SerialPort.getCommPorts();
        List<String> portIdentifiers = new ArrayList<String>(ports.length);
        for (int i = 0; i < ports.length; i++) {
            portIdentifiers.add(ports[i].getSystemPortName());
        }
        return portIdentifiers;
    }

    @Override
    public String getVersion() {
        String version = "information about version is unavailable.";
        try {
            Class.forName("purejavacomm.PureJavaComm");
            version = purejavacomm.PureJavaComm.getVersion();
        } catch (ClassNotFoundException e) {
            Modbus.log().warning("The PureJavaComm library is not found.");
        }
        return version;
    }
}
