package com.intelligt.modbus.jlibmodbus.msg.base;

import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.ModbusMessageFactory;
import com.intelligt.modbus.jlibmodbus.msg.ModbusResponseFactory;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
@SuppressWarnings("unchecked")
abstract public class ModbusRequest extends ModbusMessage implements ModbusMessageFactory {

    final private ModbusResponse response;

    public ModbusRequest() {
        ModbusResponse response = null;
        try {
            final Constructor<ModbusResponse>[] constructors = (Constructor<ModbusResponse>[]) getResponseClass().getConstructors();
            for (Constructor<ModbusResponse> c : constructors) {
                if (c.getParameterTypes().length == 0) {
                    response = c.newInstance();
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            this.response = response;
        }
    }

    @Override
    public void setServerAddress(int serverAddress) throws ModbusNumberException {
        super.setServerAddress(serverAddress);

        getResponse().setServerAddress(serverAddress);
    }

    public ModbusResponse getResponse() {
        return response;
    }

    protected abstract Class getResponseClass();

    abstract public void writeRequest(ModbusOutputStream fifo) throws IOException;

    @Override
    final public void writePDU(ModbusOutputStream fifo) throws IOException {
        fifo.write(getFunction());
        writeRequest(fifo);
    }

    @Override
    final protected int pduSize() {
        return 1 + requestSize();
    }

    abstract public int requestSize();

    abstract public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException;

    abstract protected boolean validateResponseImpl(ModbusResponse response);

    public void validateResponse(ModbusResponse msg) throws ModbusNumberException {
        if (getProtocolId() != msg.getProtocolId())
            throw new ModbusNumberException("Collision: does not matches the protocol id");
        if (getTransactionId() != msg.getTransactionId())
            throw new ModbusNumberException("Collision: does not matches the transaction id");
        if (getServerAddress() != msg.getServerAddress())
            throw new ModbusNumberException("Does not matches the slave address", msg.getServerAddress());
        if (getFunction() != msg.getFunction())
            throw new ModbusNumberException("Does not matches the function code", msg.getFunction());
        if (!msg.isException()) {
            if (!validateResponseImpl(msg))
                throw new ModbusNumberException("Collision: response does not matches the request");
        }
    }

    @Override
    public ModbusMessage createMessage(int functionCode) {
        if (functionCode != getFunction()) {
            return ModbusResponseFactory.getInstance().createMessage(functionCode);
        } else {
            return getResponse();
        }
    }
}
