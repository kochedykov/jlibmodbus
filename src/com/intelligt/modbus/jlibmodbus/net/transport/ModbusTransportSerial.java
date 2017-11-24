package com.intelligt.modbus.jlibmodbus.net.transport;

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.ModbusMessageFactory;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusMessage;
import com.intelligt.modbus.jlibmodbus.net.stream.InputStreamSerial;
import com.intelligt.modbus.jlibmodbus.net.stream.OutputStreamSerial;

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
class ModbusTransportSerial extends ModbusTransport {

    ModbusTransportSerial(InputStreamSerial is, OutputStreamSerial os) {
        super(is, os);
    }

    @Override
    protected ModbusMessage read(ModbusMessageFactory factory) throws ModbusIOException, ModbusNumberException {
        if (getInputStream() instanceof InputStreamSerial) {
            InputStreamSerial is = (InputStreamSerial) getInputStream();
            try {
                is.frameInit();
                ModbusMessage msg = createMessage(factory);
                is.frameCheck();
                return msg;
            } catch (IOException ioe) {
                throw new ModbusIOException(ioe);
            }
        } else {
            throw new ModbusIOException("Can't cast getInputStream() to InputStreamSerial");
        }
    }

    @Override
    protected void sendImpl(ModbusMessage msg) throws ModbusIOException {
        msg.write(getOutputStream());
    }
}
