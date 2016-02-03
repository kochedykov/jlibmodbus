package com.sbpinvertor.modbus;

import com.sbpinvertor.modbus.data.DataHolder;
import com.sbpinvertor.modbus.data.SimpleHoldingRegisters;

import java.io.IOException;

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

/**
 * facade
 */
abstract public class ModbusSlave {

    private int serverAddress = 1;
    private DataHolder dataHolder = new DataHolder();

    protected ModbusSlave() {
        dataHolder.setHoldingRegisters(new SimpleHoldingRegisters(1000));
    }

    /**
     * starts the ModbusSlave thread.
     */
    abstract public void open() throws IOException;

    /**
     * should have stop the thread of the ModbusSlave.
     */
    abstract public void close() throws IOException;


    /*Getters & Setters*/

    public DataHolder getDataHolder() {
        return dataHolder;
    }

    public void setDataHolder(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    public int getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(int serverAddress) {
        if (Modbus.checkServerAddress(serverAddress))
            this.serverAddress = serverAddress;
    }
}
