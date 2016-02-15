package com.invertor.modbus.data;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.data.events.ModbusEvent;

import java.util.LinkedList;
import java.util.List;

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
public class CommStatus {
    public static final int EVENT_QUEUE_CAPACITY = Modbus.MAX_PDU_LENGTH - 8;
    private int status = 0;
    private int eventCount = 0;
    private int messageCount = 0;
    /**
     * Comm event queue. Capacity = PDU length(254) - server_address - function_code - 3 x 2 Bytes,
     * (Length of Status, Event Count and Message Count).
     */
    private LinkedList<ModbusEvent> eventQueue = new LinkedList<ModbusEvent>();

    public CommStatus() {
    }

    public void incrementEventCounter() {
        eventCount++;
    }

    public void enter() {
        status = 0xffff;
    }

    public void leave() {
        status = 0;
    }

    public void incrementMessageCounter() {
        messageCount++;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public List<ModbusEvent> getEventQueue() {
        return eventQueue;
    }

    public void addEvent(ModbusEvent event) {
        if (eventQueue.size() >= EVENT_QUEUE_CAPACITY)
            eventQueue.poll();
        eventQueue.add(event);
    }
}
