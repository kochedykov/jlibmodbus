package com.sbpinvertor.modbus;

import com.sbpinvertor.conn.SerialPort;

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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

public class Modbus {
    public static final int MAX_CONNECTION_TIMEOUT = 3000;
    public static final int MAX_RESPONSE_TIMEOUT = 1000;
    public static final int MIN_MESSAGE_LENGTH = 3;// Modbus exception = serverAddress + functionCode + ExceptionCode
    public static final int MAX_PDU_LENGTH = 254;
    public static final int MAX_TCP_ADU_LENGTH = 260;
    public static final int MAX_RTU_ADU_LENGTH = 256;
    public static final int MAX_REGISTER_VALUE = 0xFFFF;
    public static final int MIN_START_ADDRESS = 0x0000;
    public static final int MAX_START_ADDRESS = 0xFFFF;
    public static final int MAX_READ_COIL_COUNT = 0x7D0;
    public static final int MAX_WRITE_COIL_COUNT = 0x7B0;
    public static final int MAX_READ_REGISTER_COUNT = 0x7D;
    public static final int MAX_WRITE_REGISTER_COUNT = 0x7B;
    public static final int MIN_SERVER_ADDRESS = 1;
    public static final int MAX_SERVER_ADDRESS = 247;
    public static final int TCP_PORT = 502;
    public static final int PROTOCOL_ID = 0;

    private Modbus() {

    }

    /**
     * validates address of server
     * @param serverAddress - The modbus server address
     * @return "true" if serverAddress is correct, else "false".
     */
    static public boolean checkServerAddress(int serverAddress) {
        /*
        * hook for server address is equals zero:
        * some of modbus tcp slaves sets the UnitId value to zero, not ignoring value in this field.
        */
        if (0 == serverAddress) {
            Logger.getAnonymousLogger().warning("Server address must be in range from " + MIN_SERVER_ADDRESS + " to " + MAX_SERVER_ADDRESS);
            return true;
        }
        return !(serverAddress < MIN_SERVER_ADDRESS || serverAddress > MAX_SERVER_ADDRESS);
    }

    static private boolean checkRange(int value, int min, int max) {
        return !(value < min || value > max);
    }

    static private boolean checkQuantity(int value, int max) {
        return checkRange(value, 1, max);
    }

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

    static public ModbusMaster createModbusMasterRTU(String device, SerialPort.BaudRate baudRate, int dataBits, int stopBits, SerialPort.Parity parity) {
        ModbusMaster m = null;
        try {
            m = new ModbusMasterRTU(device, baudRate, dataBits, stopBits, parity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }

    static public ModbusMaster createModbusMasterTCP(String host, boolean keepAlive) {
        return createModbusMasterTCP(host, Modbus.TCP_PORT, keepAlive);
    }

    static public ModbusMaster createModbusMasterTCP(String host) {
        return createModbusMasterTCP(host, false);
    }

    static public ModbusMaster createModbusMasterTCP(String host, int port, boolean keepAlive) {
        ModbusMaster m = null;
        try {
            m = new ModbusMasterTCP(host, port, keepAlive);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }
}
