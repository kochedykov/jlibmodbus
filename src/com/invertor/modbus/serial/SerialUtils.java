package com.invertor.modbus.serial;

import java.util.List;

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

    static private SerialPortAbstractFactory factory;

    static {
        factory = new SerialPortFactoryJSSC();
    }

    /**
     * @param factory - a concrete serial port factory instance
     * @since 1.2.1
     */
    static public void setSerialPortFactory(SerialPortAbstractFactory factory) {
        SerialUtils.factory = factory;
    }

    static public SerialPort createSerial(SerialParameters sp) throws SerialPortException {
        return factory.createSerial(sp);
    }

    static public List<String> getPortIdentifiers() {
        return factory.getPortIdentifiers();
    }

    static public String getConnectorVersion() {
        return factory.getVersion();
    }
}
