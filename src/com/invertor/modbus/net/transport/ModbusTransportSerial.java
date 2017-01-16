package com.invertor.modbus.net.transport;

import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.ModbusMessageFactory;
import com.invertor.modbus.msg.base.ModbusMessage;
import com.invertor.modbus.net.stream.InputStreamSerial;
import com.invertor.modbus.net.stream.OutputStreamSerial;

import java.io.IOException;
import java.io.InputStream;

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
        ModbusMessage msg;
        InputStream is = getInputStream();
        if (!(is instanceof InputStreamSerial))
            throw new ModbusIOException("Can't cast getInputStream() to InputStreamSerial");
        InputStreamSerial iss = (InputStreamSerial) is;
        try {
            iss.frameInit();
            msg = factory.createMessage(iss);
            iss.frameCheck();
        } catch (IOException ioe) {
            throw new ModbusIOException(ioe);
        }
        return msg;
    }

    @Override
    protected void sendImpl(ModbusMessage msg) throws ModbusIOException {
        msg.write(getOutputStream());
    }
}
