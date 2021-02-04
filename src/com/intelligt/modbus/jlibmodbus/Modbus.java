package com.intelligt.modbus.jlibmodbus;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

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
final public class Modbus {
    final static public int MAX_CONNECTION_TIMEOUT = 3000;
    final static public int MAX_RESPONSE_TIMEOUT = 1000;
    final static public int MAX_PDU_LENGTH = 254;
    /*
       slave id + function code.
       Note that actually the modbus pdu doesn't include the "slave id" field.
     */
    final static public int MIN_PDU_LENGTH = 2;
    final static public int MAX_TCP_ADU_LENGTH = 260;
    final static public int MAX_RTU_ADU_LENGTH = 256;
    final static public int MAX_REGISTER_VALUE = 0xFFFF;
    final static public int MIN_START_ADDRESS = 0x0000;
    final static public int MAX_START_ADDRESS = 0xFFFF;
    final static public int MAX_READ_COIL_COUNT = 0x7D0;
    final static public int MAX_WRITE_COIL_COUNT = 0x7B0;
    final static public int MAX_READ_REGISTER_COUNT = 0x7D;
    final static public int MAX_WRITE_REGISTER_COUNT = 0x7B;
    final static public int MAX_FIFO_COUNT = 31;
    final static public int MIN_SERVER_ADDRESS = 1;
    final static public int MAX_SERVER_ADDRESS = 247;
    final static public int TCP_PORT = 502;
    final static public int PROTOCOL_ID = 0;
    final static public int TCP_DEFAULT_ID = 0xFF;
    final static public int BROADCAST_ID = 0x00;
    final static public int ASCII_CODE_CR = 0xd;
    final static public int ASCII_CODE_LF = 0xa;
    final static public int ASCII_CODE_COLON = 0x3a;
    final static public int COIL_VALUE_ON = 0xff00;
    final static public int COIL_VALUE_OFF = 0x0000;
    final static public int TRANSACTION_ID_MAX_VALUE = 0xFFFF;
    final static public int DEFAULT_READ_TIMEOUT = 1000;

    final static private Logger log = Logger.getLogger(Modbus.class.getName());
    static private LogLevel logLevel = LogLevel.LEVEL_RELEASE;
    static private boolean autoIncrementTransactionId = false;

    /**
     * the end of message delimiter, (LF character by default)
     */
    static private int asciiMsgDelimiter = Modbus.ASCII_CODE_LF;

    static {
        setLogLevel(logLevel);
        log().setUseParentHandlers(false);
        log().addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                System.out.println(record.getLevel().getName() + ": " + record.getMessage());
            }

            @Override
            public void flush() {
                //do nothing
            }

            @Override
            public void close() throws SecurityException {
                //do nothing
            }
        });
    }

    private Modbus() {
    }

    /**
     * getter for the version of the library.
     *
     * @return a string representing the version
     */
    static public String getVersion() {
        return Version.getVersion();
    }

    static public void setAsciiInputDelimiter(int asciiMsgDelimiter) {
        Modbus.asciiMsgDelimiter = asciiMsgDelimiter;
    }

    static public int getAsciiMsgDelimiter() {
        return asciiMsgDelimiter;
    }

    /**
     * getter for log level
     *
     * @return current log level
     */
    static public LogLevel getLogLevel() {
        return logLevel;
    }

    /**
     * changes the log level for all loggers used
     *
     * @param level - LogLevel instance
     * @see Modbus.LogLevel
     */
    static public void setLogLevel(LogLevel level) {
        logLevel = level;
        log.setLevel(level.value());
        for (Handler handler : log.getHandlers()) {
            handler.setLevel(level.value());
        }
    }

    static public boolean isLoggingEnabled() {
        return getLogLevel() != LogLevel.LEVEL_RELEASE;
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
        switch (serverAddress) {
            case 0x00:
                //Modbus.log().info("Broadcast message");
                return true;
            case 0xFF:
                //Modbus.log().info("Using 0xFF ModbusTCP default slave id");
                return true;
            default:
                return !(serverAddress < Modbus.MIN_SERVER_ADDRESS || serverAddress > Modbus.MAX_SERVER_ADDRESS);
        }
    }

    /**
     * validates is the value in the range from min to max.
     *
     * @param value - the value for checkFrame
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
     * @param value - the value for checkFrame
     * @param max   - maximum
     * @return "true" if value in the range from 1 to max, else "false".
     */
    static private boolean checkQuantity(int value, int max) {
        return checkRange(value, 1, max);
    }

    /**
     * validates data offset in the range from Modbus.MIN_START_ADDRESS to max.
     *
     * @param value - the offset for checkFrame
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
     * returns autoIncrementTransactionId variable
     *
     * @return "true" if autoincrement of TransactionId field is enabled, else "false".
     */
    public static boolean isAutoIncrementTransactionId() {
        return Modbus.autoIncrementTransactionId;
    }

    /**
     * activates auto increment of TransactionId field. if you do not use ModbusTCP, invocation has no effect.
     *
     * @param autoIncrementTransactionId - new value of the autoIncrementTransactionId variable
     */
    public static void setAutoIncrementTransactionId(boolean autoIncrementTransactionId) {
        Modbus.autoIncrementTransactionId = autoIncrementTransactionId;
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
