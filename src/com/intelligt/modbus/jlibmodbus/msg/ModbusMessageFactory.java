package com.intelligt.modbus.jlibmodbus.msg;

import com.intelligt.modbus.jlibmodbus.msg.base.ModbusMessage;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusRequest;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;

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
public interface ModbusMessageFactory {
    /**
     * This method creates a #ModbusMessage instance from #functionCode.
     * The returned value can be either an instance of ModbusRequest or ModbusResponse.
     *
     * @param functionCode a number representing a modbus function
     * @return an instance of a specific ModbusMessage
     * @see ModbusRequest
     * @see ModbusResponse
     * @see ModbusMessageFactory
     * @see ModbusResponseFactory
     */
    ModbusMessage createMessage(int functionCode);
}
