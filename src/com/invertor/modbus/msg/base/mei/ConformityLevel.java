package com.invertor.modbus.msg.base.mei;

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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public enum ConformityLevel {
    BASIC_STREAM_ONLY(0x01),
    REGULAR_STREAM_ONLY(0x02),
    EXTENDED_STREAM_ONLY(0x03),
    BASIC_STREAM_AND_INDIVIDUAL(0x81),
    REGULAR_STREAM_AND_INDIVIDUAL(0x82),
    EXTENDED_STREAM_AND_INDIVIDUAL(0x83);

    final private int code;

    ConformityLevel(int code) {
        this.code = code;
    }

    static public ConformityLevel get(int code) {
        for (ConformityLevel c : values()) {
            if (c.toInt() == code) {
                return c;
            }
        }
        return BASIC_STREAM_ONLY;
    }

    public int toInt() {
        return code;
    }
}
