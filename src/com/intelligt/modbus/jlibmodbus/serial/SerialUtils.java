package com.intelligt.modbus.jlibmodbus.serial;

import com.intelligt.modbus.jlibmodbus.Modbus;

import java.util.*;

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
public class SerialUtils {

    static private Set<ValidatorSerialPortFactory> validatorSet = new TreeSet<ValidatorSerialPortFactory>();

    static {
        registerSerialPortFactory("com.fazecast.jSerialComm.SerialPort", "com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryJSerialComm");
        registerSerialPortFactory("jssc.SerialPort", "com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryJSSC");
        registerSerialPortFactory("purejavacomm.PureJavaComm", "com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryPJC");
        registerSerialPortFactory("gnu.io.RXTXVersion", "com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryRXTX");

        registerSerialPortFactory("com.google.android.things.AndroidThings", "com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryAT");
    }

    static private SerialPortAbstractFactory factory = null;

    static public void registerSerialPortFactory(String connectorClassname, String factoryClassname) {
        if (!validatorSet.add(new ValidatorSerialPortFactory(connectorClassname, factoryClassname))) {
            Modbus.log().warning("The factory is already registered, skipping: " + factoryClassname);
        }
    }

    static public void trySelectConnector() throws SerialPortException {
        Iterator<ValidatorSerialPortFactory> iterator = validatorSet.iterator();
        while (iterator.hasNext() && getSerialPortFactory() == null) {
            ValidatorSerialPortFactory validator = iterator.next();
            if (validator.validate() ) {
                try {
                    setSerialPortFactory(validator.getFactory());
                } catch (ClassNotFoundException e) {
                    Modbus.log().warning("Cannot set a serial port factory " + validator.getFactoryClassname());
                }
            }
        }
        if (getSerialPortFactory() == null) {
            throw new SerialPortException("There are no available connectors");
        }
    }

    static public SerialPort createSerial(SerialParameters sp) throws SerialPortException {
        return getSerialPortFactory().createSerial(sp);
    }

    static public List<String> getPortIdentifiers() throws SerialPortException {
        return getSerialPortFactory().getPortIdentifiers();
    }

    static public String getConnectorVersion() {
        return getSerialPortFactory().getVersion();
    }

    /**
     * @param factory - a concrete serial port factory instance
     * @since 1.2.1
     */
    static public void setSerialPortFactory(SerialPortAbstractFactory factory) {
        SerialUtils.factory = factory;
    }

    static public SerialPortAbstractFactory getSerialPortFactory() {
        if (SerialUtils.factory == null){
            SerialUtils.factory = new SerialPortFactoryJSSC();
        }
        return SerialUtils.factory;
    }
}
