package com.invertor.modbus.data.events;

/**
 * Copyright (c) 2015-2016 JSC Invertor
 * [http://www.sbp-invertor.ru]
 * <p/>
 * This file is part of JLibModbus.
 * <p/>
 * JLibModbus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ModbusEventReceive extends ModbusEvent {
    public static final int BIT_EVENT_TYPE = 7;
    final static private int BIT_COMMUNICATION_ERROR = 1;
    final static private int BIT_CHARACTER_OVERRUN = 4;
    final static private int BIT_CURRENTLY_IN_LISTEN_ONLY_MODE = 5;
    final static private int BIT_BROADCAST_RECEIVED = 5;

    private ModbusEventReceive(int bit) {
        setBit(BIT_EVENT_TYPE);
        setBit(bit);
    }

    static public ModbusEventReceive createCommunicationError() {
        return new ModbusEventReceive(BIT_COMMUNICATION_ERROR);
    }

    static public ModbusEventReceive createCharacterOverrun() {
        return new ModbusEventReceive(BIT_CHARACTER_OVERRUN);
    }

    static public ModbusEventReceive createCurrentlyInListenOnlyMode() {
        return new ModbusEventReceive(BIT_CURRENTLY_IN_LISTEN_ONLY_MODE);
    }

    static public ModbusEventReceive createBroadcastReceived() {
        return new ModbusEventReceive(BIT_BROADCAST_RECEIVED);
    }

    public boolean isCurrentlyInListenOnlyMode() {
        return isBitSet(BIT_CURRENTLY_IN_LISTEN_ONLY_MODE);
    }

    public boolean isCharacterOverrun() {
        return isBitSet(BIT_CHARACTER_OVERRUN);
    }

    public boolean isCommunicationError() {
        return isBitSet(BIT_COMMUNICATION_ERROR);
    }

    public boolean isBroadcastReceived() {
        return isBitSet(BIT_BROADCAST_RECEIVED);
    }
}