package com.intelligt.modbus.jlibmodbus.serial;

import java.util.Arrays;
import java.util.List;

/*
 * Copyright (C) 2017 "Invertor" Factory", JSC
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
 * Authors: Kevin Kieffer.
 * email: <kkieffer@adaptivemethods.com>
 */

/**
 * @author Kevin Kieffer
 * @since 1.9.0
 */
public class SerialPortFactoryLoopback extends SerialPortAbstractFactory {

    final private boolean isMaster;

    public SerialPortFactoryLoopback(boolean isMaster) {
        this.isMaster = isMaster;
    }

    @Override
    public SerialPort createSerialImpl(SerialParameters sp) throws SerialPortException {
        return new SerialPortLoopback(sp, isMaster);
    }

    @Override
    public List<String> getPortIdentifiersImpl() {
        return Arrays.asList(new String[0]);
    }
}
