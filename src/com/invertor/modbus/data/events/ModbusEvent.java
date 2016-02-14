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

    private int event = 0;

    public ModbusEvent() {
    }

    protected void setBit(int bit) {
        if (bit > 7 || bit < 0) {
            throw new IllegalArgumentException(String.format("bit must be in range from 0 to 7, not %d.", bit));
        }
        event |= 1 << bit;
    }

    protected void resetBit(int bit) {
        if (bit > 7 || bit < 0) {
            throw new IllegalArgumentException(String.format("bit must be in range from 0 to 7, not %d.", bit));
        }
        event &= ~(1 << bit);
    }

    protected boolean isBitSet(int bit) {
        return (event & (1 << bit)) != 0;
    }

    public int getEvent() {
        return event;
    }
}