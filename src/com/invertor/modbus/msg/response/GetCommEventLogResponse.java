package com.invertor.modbus.msg.response;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.data.events.ModbusEvent;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.ModbusResponse;
import com.invertor.modbus.net.stream.base.ModbusInputStream;
import com.invertor.modbus.net.stream.base.ModbusOutputStream;
import com.invertor.modbus.utils.ModbusFunctionCode;

import java.io.IOException;
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
final public class GetCommEventLogResponse extends ModbusResponse {

    public static final int EVENTS_OFFSET = 6;
    private int status;
    private int eventCount;
    private int messageCount;
    private List<ModbusEvent> events = new LinkedList<ModbusEvent>();

    public GetCommEventLogResponse(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
    }

    @Override
    protected void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        int byteCount = fifo.read();
        if (byteCount < EVENTS_OFFSET || byteCount > Modbus.MAX_PDU_LENGTH - 2)
            throw new ModbusNumberException("Illegal byte count", byteCount);
        setStatus(fifo.readShortBE());
        setEventCount(fifo.readShortBE());
        setMessageCount(fifo.readShortBE());
        int eventsCount = byteCount - EVENTS_OFFSET;
        if (eventsCount > 0) {
            for (int i = 0; i < eventsCount; i++) {
                events.add(ModbusEvent.getEvent(fifo.read()));
            }
        }
    }

    @Override
    protected void writeResponse(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getStatus());
        fifo.writeShortBE(getEventCount());
        fifo.writeShortBE(getMessageCount());
    }

    @Override
    protected int responseSize() {
        return EVENTS_OFFSET + events.size();
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.GET_COMM_EVENT_COUNTER.toInt();
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

    public List<ModbusEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ModbusEvent> events) {
        this.events = events;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }
}
