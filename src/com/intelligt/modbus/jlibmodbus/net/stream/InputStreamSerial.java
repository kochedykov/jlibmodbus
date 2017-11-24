package com.intelligt.modbus.jlibmodbus.net.stream;

import com.intelligt.modbus.jlibmodbus.exception.ModbusChecksumException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.LoggingInputStream;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;

import java.io.IOException;

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
public abstract class InputStreamSerial extends LoggingInputStream {

    InputStreamSerial(SerialPort serial) {
        super(serial.getInputStream());
    }

    /**
     * transport invokes it for validation of each frame
     *
     * @throws IOException             when there is any communication trouble
     * @throws ModbusChecksumException when invalid frame has received
     */
    abstract public void frameCheck() throws IOException, ModbusChecksumException;

    /**
     * it should be invoked before reading of a frame.
     *
     * @throws IOException when there is any communication trouble
     */
    abstract public void frameInit() throws IOException;
}
