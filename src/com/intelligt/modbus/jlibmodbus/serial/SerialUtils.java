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
    final static private SerialPortFactoryJSSC FACTORY_JSSC = new SerialPortFactoryJSSC();
    final static private SerialPortFactoryRXTX FACTORY_RXTX = new SerialPortFactoryRXTX();
    final static private SerialPortFactoryJSerialComm FACTORY_JSERIAL_COMM = new SerialPortFactoryJSerialComm();
    final static private SerialPortFactoryPJC FACTORY_PJC = new SerialPortFactoryPJC();
    final static private SerialPortFactoryAT FACTORY_AT = new SerialPortFactoryAT();
    @Deprecated
    final static private SerialPortFactoryJavaComm FACTORY_JAVA_COMM = new SerialPortFactoryJavaComm();

    final static private SerialPortAbstractFactory[] SERIAL_FACTORIES = new SerialPortAbstractFactory[] {
            FACTORY_JSERIAL_COMM, FACTORY_JSSC, FACTORY_RXTX, FACTORY_PJC, FACTORY_AT, FACTORY_JAVA_COMM};


    /*
    static public boolean isThingsDevice(Context context) {
        final PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_EMBEDDED);
    }
    */

    static {
        for (SerialPortAbstractFactory f : SERIAL_FACTORIES) {
            if (f.available()) {
                setSerialPortFactory(f);
                break;
            }
        }
    }

    static public void setSerialPortFactoryJSSC() {
        setSerialPortFactory(FACTORY_JSSC);
    }

    static public void setSerialPortFactoryRXTX() {
        setSerialPortFactory(FACTORY_RXTX);
    }

    static public void setSerialPortFactoryJSerialComm() {
        setSerialPortFactory(FACTORY_JSERIAL_COMM);
    }

    static public void setSerialPortFactoryPureJavaComm() {
        setSerialPortFactory(FACTORY_PJC);
    }

    static public void setSerialPortFactoryAndroidThings() {
        setSerialPortFactory(FACTORY_AT);
    }

    @Deprecated
    static public void setSerialPortFactoryJavaComm() {
        SerialUtils.factory = FACTORY_JAVA_COMM;
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
