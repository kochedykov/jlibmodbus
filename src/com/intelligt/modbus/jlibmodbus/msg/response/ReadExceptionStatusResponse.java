package com.intelligt.modbus.jlibmodbus.msg.response;

import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.ModbusFunctionCode;

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
final public class ReadExceptionStatusResponse extends ModbusResponse {

    private int exceptionStatus = 0;

    public ReadExceptionStatusResponse() {
        super();
    }

    public int getExceptionStatus() {
        return exceptionStatus;
    }

    public void setExceptionStatus(int exceptionStatus) {
        this.exceptionStatus = (byte) exceptionStatus & 0xff;
    }

    @Override
    protected void readResponse(ModbusInputStream fifo) throws IOException {
        setExceptionStatus(fifo.read());
    }

    @Override
    protected void writeResponse(ModbusOutputStream fifo) throws IOException {
        fifo.write(getExceptionStatus());
    }

    @Override
    protected int responseSize() {
        return 1;
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.READ_EXCEPTION_STATUS.toInt();
    }
}
