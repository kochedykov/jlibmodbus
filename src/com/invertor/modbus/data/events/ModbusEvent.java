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
abstract public class ModbusEvent {

    final private Type type;
    private int event = 0;

    public ModbusEvent(Type type, int event) {
        this.type = type;
        this.event = type.getCode() | event;
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

    ModbusEvent getEvent(int event) {
        for (Type t : Type.values()) {
            if ((t.getCode() & event) == t.getCode()) {
                switch (t) {
                    case SEND:
                        return new ModbusEventSend(event);
                    case RECEIVE:
                        return new ModbusEventReceive(event);
                    case INITIATED_COMMUNICATION_RESTART:
                        return new ModbusEventInitiatedCommunicationRestart(event);
                    case ENTER_LISTEN_ONLY_MODE:
                        return new ModbusEventEnterListenOnlyMode(event);
                }
            }
        }
        throw new IllegalArgumentException();
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