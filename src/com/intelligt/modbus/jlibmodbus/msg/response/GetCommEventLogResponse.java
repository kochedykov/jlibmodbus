package com.intelligt.modbus.jlibmodbus.msg.response;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.comm.ModbusCommEvent;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.msg.base.ModbusResponse;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.ModbusFunctionCode;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
final public class GetCommEventLogResponse extends ModbusResponse {

    public static final int EVENTS_OFFSET = 6;
    private int status;
    private int eventCount;
    private int messageCount;
    private List<ModbusCommEvent> events = new LinkedList<ModbusCommEvent>();

    public GetCommEventLogResponse() {
        super();
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
                events.add(ModbusCommEvent.getEvent(fifo.read()));
            }
        }
    }

    @Override
    protected void writeResponse(ModbusOutputStream fifo) throws IOException {
        fifo.write(EVENTS_OFFSET + events.size());
        fifo.writeShortBE(getStatus());
        fifo.writeShortBE(getEventCount());
        fifo.writeShortBE(getMessageCount());
        for (ModbusCommEvent event : getEventQueue()) {
            fifo.write(event.getEvent());
        }
    }

    @Override
    protected int responseSize() {
        return EVENTS_OFFSET + events.size();
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.GET_COMM_EVENT_LOG.toInt();
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

    public List<ModbusCommEvent> getEventQueue() {
        return events;
    }

    public void setEvents(List<ModbusCommEvent> events) {
        this.events = events;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }
}
