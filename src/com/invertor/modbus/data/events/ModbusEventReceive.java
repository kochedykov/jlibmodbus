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
    final static private int BIT_COMMUNICATION_ERROR = 0x1;
    final static private int BIT_CHARACTER_OVERRUN = 0x10;
    final static private int BIT_CURRENTLY_IN_LISTEN_ONLY_MODE = 0x20;
    final static private int BIT_BROADCAST_RECEIVED = 0x40;

    protected ModbusEventReceive(int event) {
        super(Type.RECEIVE, event);
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