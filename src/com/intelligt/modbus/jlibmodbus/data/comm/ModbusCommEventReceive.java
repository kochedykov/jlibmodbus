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
public class ModbusCommEventReceive extends ModbusCommEvent {
    final static private int BIT_COMMUNICATION_ERROR = 0x1;
    final static private int BIT_CHARACTER_OVERRUN = 0x10;
    final static private int BIT_CURRENTLY_IN_LISTEN_ONLY_MODE = 0x20;
    final static private int BIT_BROADCAST_RECEIVED = 0x40;

    protected ModbusCommEventReceive(int event) {
        super(Type.RECEIVE, event);
    }

    static public ModbusCommEventReceive createCommunicationError() {
        return new ModbusCommEventReceive(BIT_COMMUNICATION_ERROR);
    }

    static public ModbusCommEventReceive createCharacterOverrun() {
        return new ModbusCommEventReceive(BIT_CHARACTER_OVERRUN);
    }

    static public ModbusCommEventReceive createCurrentlyInListenOnlyMode() {
        return new ModbusCommEventReceive(BIT_CURRENTLY_IN_LISTEN_ONLY_MODE);
    }

    static public ModbusCommEventReceive createBroadcastReceived() {
        return new ModbusCommEventReceive(BIT_BROADCAST_RECEIVED);
    }

    public boolean isCurrentlyInListenOnlyMode() {
        return isBitsSet(BIT_CURRENTLY_IN_LISTEN_ONLY_MODE);
    }

    public boolean isCharacterOverrun() {
        return isBitsSet(BIT_CHARACTER_OVERRUN);
    }

    public boolean isCommunicationError() {
        return isBitsSet(BIT_COMMUNICATION_ERROR);
    }

    public boolean isBroadcastReceived() {
        return isBitsSet(BIT_BROADCAST_RECEIVED);
    }
}