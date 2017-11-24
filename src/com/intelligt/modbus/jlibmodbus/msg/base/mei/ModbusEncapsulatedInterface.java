package com.intelligt.modbus.jlibmodbus.msg.base.mei;

import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.MEITypeCode;

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
public interface ModbusEncapsulatedInterface {
    MEITypeCode getTypeCode();

    void writeRequest(ModbusOutputStream fifo) throws IOException;

    void readRequest(ModbusInputStream fifo) throws IOException;

    int getRequestSize();

    void writeResponse(ModbusOutputStream fifo) throws IOException;

    void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException;

    int getResponseSize();

    void process(DataHolder dataHolder);
}
