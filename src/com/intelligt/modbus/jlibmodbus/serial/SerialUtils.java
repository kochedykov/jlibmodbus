package com.intelligt.modbus.jlibmodbus.serial;

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

    /*
    static public boolean isThingsDevice(Context context) {
        final PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_EMBEDDED);
    }
    */

    static public void setSerialPortFactoryJSSC() {
        setSerialPortFactory(new SerialPortFactoryJSSC());
    }

    static public void setSerialPortFactoryRXTX() {
        setSerialPortFactory(new SerialPortFactoryRXTX());
    }

    static public void setSerialPortFactoryJSerialComm() {
        setSerialPortFactory(new SerialPortFactoryJSerialComm());
    }

    static public void setSerialPortFactoryPureJavaComm() {
        setSerialPortFactory(new SerialPortFactoryPJC());
    }

    static public void setSerialPortFactoryAndroidThings() {
        setSerialPortFactory(new SerialPortFactoryAT());
    }

    @Deprecated
    static public void setSerialPortFactoryJavaComm() {
        setSerialPortFactory(new SerialPortFactoryJavaComm());
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
        return SerialUtils.factory;
    }
}
