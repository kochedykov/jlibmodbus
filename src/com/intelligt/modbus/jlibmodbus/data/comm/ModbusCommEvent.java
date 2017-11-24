package com.intelligt.modbus.jlibmodbus.data.comm;

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
abstract public class ModbusCommEvent {

    final private Type type;
    private int event = 0;

    public ModbusCommEvent(Type type, int event) {
        this.type = type;
        this.event = type.getCode() | event;
    }

    static public ModbusCommEvent getEvent(int event) {
        for (Type t : Type.values()) {
            if ((t.getCode() & event) == t.getCode()) {
                switch (t) {
                    case SEND:
                        return new ModbusCommEventSend(event);
                    case RECEIVE:
                        return new ModbusCommEventReceive(event);
                    case INITIATED_COMMUNICATION_RESTART:
                        return new ModbusCommEventInitiatedCommunicationRestart(event);
                    case ENTER_LISTEN_ONLY_MODE:
                        return new ModbusCommEventEnterListenOnlyMode(event);
                }
            }
        }
        throw new IllegalArgumentException();
    }

    public Type getType() {
        return type;
    }

    protected boolean isBitsSet(int bits) {
        return (event & bits) == bits;
    }

    public int getEvent() {
        return event;
    }

    public enum Type {
        SEND(0x80),
        RECEIVE(0x40),
        INITIATED_COMMUNICATION_RESTART(0x0),
        ENTER_LISTEN_ONLY_MODE(0x10);

        final private int code;

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}