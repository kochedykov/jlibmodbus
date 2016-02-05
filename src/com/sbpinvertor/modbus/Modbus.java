package com.sbpinvertor.modbus;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright (c) 2015-2016 JSC "Zavod "Invertor"
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
final public class Modbus {
    final static public int MAX_CONNECTION_TIMEOUT = 3000;
    final static public int MAX_RESPONSE_TIMEOUT = 1000;
    final static public int MAX_PDU_LENGTH = 254;
    final static public int MIN_PDU_LENGTH = 3;
    //final static public int MAX_TCP_ADU_LENGTH = 260;
    final static public int MAX_RTU_ADU_LENGTH = 256;
    final static public int MAX_REGISTER_VALUE = 0xFFFF;
    final static public int MIN_START_ADDRESS = 0x0000;
    final static public int MAX_START_ADDRESS = 0xFFFF;
    final static public int MAX_READ_COIL_COUNT = 0x7D0;
    final static public int MAX_WRITE_COIL_COUNT = 0x7B0;
    final static public int MAX_READ_REGISTER_COUNT = 0x7D;
    final static public int MAX_WRITE_REGISTER_COUNT = 0x7B;
    final static public int MIN_SERVER_ADDRESS = 1;
    final static public int MAX_SERVER_ADDRESS = 247;
    final static public int TCP_PORT = 502;
    final static public int PROTOCOL_ID = 0;
    final static public int ASCII_CODE_CR = 0xd;
    final static public int ASCII_CODE_LF = 0xa;
    final static public int ASCII_CODE_COLON = 0x3a;
    final static public int COIL_VALUE_ON = 0xff00;
    final static public int COIL_VALUE_OFF = 0x0000;
    final static private Logger log = Logger.getLogger(Modbus.class.getName());

    static {
        setLogLevel(LogLevel.LEVEL_RELEASE);
    }

    private Modbus() {
    }

    /**
     * changes the log level for all loggers used
     *
     * @param level - LogLevel instance
     * @see LogLevel
     */
    static public void setLogLevel(LogLevel level) {
        log.setLevel(level.value());
        for (Handler handler : log.getHandlers()) {
            handler.setLevel(level.value());
        }
    }

    /**
     * getter for default logger
     *
     * @return default logger
     */
    static public Logger log() {
        return log;
    }

    /**
     * converts ON/OFF value to boolean
     *
     * @param s 0xFF00 of 0x0000
     * @return true if s equals 0xFF00, else false
     */
    static public boolean toCoil(int s) {
        return (((short) s) & 0xffff) == Modbus.COIL_VALUE_ON;
    }

    /**
     * converts boolean to ON/OFF value
     *
     * @param c a coil value
     * @return 0xFF00 if coil value is true, else 0x0000
     */
    static public int fromCoil(boolean c) {
        return c ? Modbus.COIL_VALUE_ON : Modbus.COIL_VALUE_OFF;
    }

    /**
     * validates address of server
     *
     * @param serverAddress - The modbus server address
     * @return "true" if serverAddress is correct, else "false".
     */
    static public boolean checkServerAddress(int serverAddress) {
        /*
        * hook for server address is equals zero:
        * some of modbus tcp slaves sets the UnitId value to zero, not ignoring value in this field.
        */
        if (0 == serverAddress) {
            Modbus.log().warning("Server address must be in range from " + Modbus.MIN_SERVER_ADDRESS + " to " + Modbus.MAX_SERVER_ADDRESS);
            return true;
        }
        return !(serverAddress < Modbus.MIN_SERVER_ADDRESS || serverAddress > Modbus.MAX_SERVER_ADDRESS);
    }

    /**
     * validates is the value in the range from min to max.
     *
     * @param value - the value for check
     * @param min   - minimum
     * @param max   - maximum
     * @return "true" if value in the range from min to max, else "false".
     */
    static private boolean checkRange(int value, int min, int max) {
        return !(value < min || value > max);
    }

    /**
     * validates the value in the range from 1 to max.
     *
     * @param value - the value for check
     * @param max   - maximum
     * @return "true" if value in the range from 1 to max, else "false".
     */
    static private boolean checkQuantity(int value, int max) {
        return checkRange(value, 1, max);
    }

    /**
     * validates data offset in the range from Modbus.MIN_START_ADDRESS to max.
     *
     * @param value - the offset for check
     * @param max   - maximum
     * @return "true" if offset in the range from Modbus.MIN_START_ADDRESS to max, else "false".
     */
    static private boolean checkStartAddress(int value, int max) {
        return checkRange(value, MIN_START_ADDRESS, max);
    }

    /**
     * validates number of registers for read
     *
     * @param value - register count
     * @return "true" if quantity is correct, else "false".
     */
    static public boolean checkReadRegisterCount(int value) {
        return checkQuantity(value, Modbus.MAX_READ_REGISTER_COUNT);
    }

    /**
     * validates number of registers for write
     *
     * @param value - register count
     * @return "true" if quantity is correct, else "false".
     */
    static public boolean checkWriteRegisterCount(int value) {
        return checkQuantity(value, Modbus.MAX_WRITE_REGISTER_COUNT);
    }

    /**
     * validates number of coils for read
     *
     * @param value - coil count
     * @return "true" if quantity is correct, else "false".
     */
    static public boolean checkReadCoilCount(int value) {
        return checkQuantity(value, Modbus.MAX_READ_COIL_COUNT);
    }

    /**
     * validates number of coils for write
     *
     * @param value - coil count
     * @return "true" if quantity is correct, else "false".
     */
    static public boolean checkWriteCoilCount(int value) {
        return checkQuantity(value, Modbus.MAX_WRITE_COIL_COUNT);
    }

    /**
     * validates start address for read/write
     *
     * @param value - start address
     * @return "true" if start address is correct, else "false".
     */
    static public boolean checkStartAddress(int value) {
        return checkStartAddress(value, Modbus.MAX_START_ADDRESS);
    }

    /**
     * validates end address for read/write
     * end address = start address + quantity
     *
     * @param value - end address
     * @return "true" if end address is correct, else "false".
     */
    static public boolean checkEndAddress(int value) {
        return checkStartAddress(value, Modbus.MAX_START_ADDRESS + 1);
    }

    /**
     * validates register value
     *
     * @param value - value of register
     * @return "true" if register value is correct, else "false".
     */
    static public boolean checkRegisterValue(int value) {
        return checkRange(value, 0, Modbus.MAX_REGISTER_VALUE);
    }

    /**
     * logging.Level wrapper.
     */
    public enum LogLevel {
        LEVEL_RELEASE(Level.SEVERE),
        LEVEL_WARNINGS(Level.WARNING),
        LEVEL_VERBOSE(Level.INFO),
        LEVEL_DEBUG(Level.ALL);

        final private Level value;

        LogLevel(Level value) {
            this.value = value;
        }

        /**
         * getter
         *
         * @return logging.Level instance
         */
        private Level value() {
            return value;
        }
    }
}
