package com.intelligt.modbus.jlibmodbus.data;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.data.comm.ModbusCommEvent;

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
public class CommStatus {
    /**
     * PDU length(254) - server_address - function_code - 3 x 2 Bytes
     */
    public static final int EVENT_QUEUE_CAPACITY = Modbus.MAX_PDU_LENGTH - 8;
    /**
     * When the remote device enters its Listen Only Mode, all active communication controls are
     * turned off. The Ready watchdog timer is allowed to expire, locking the controls off. While the
     * device is in this mode, any MODBUS messages addressed to it or broadcast are monitored,
     * but no actions will be taken and no responses will be sent.
     */
    private boolean listenOnlyMode = false;

    volatile private boolean restartCommunicationsOption = false;

    volatile private boolean clearLog = false;
    /**
     *
     */
    private int diagnosticRegister = 0;
    /**
     * the status word will be all ones (FF FF hex) if a previously–issued program command is still being
     * processed by the remote device (a busy condition exists). Otherwise, the status word will be all zeros.
     */
    private int commStatus = 0;
    /**
     * event counter is incremented once for each successful message completion. It
     * is not incremented for exception responses, poll commands, or fetch event counter
     * commands.
     */
    private int eventCount = 0;
    /**
     * the quantity of messages that the remote device has detected
     * on the communications system since its last restart
     */
    private int busMessageCount = 0;
    /**
     * the quantity of messages addressed to the remote device, or
     * broadcast, that the remote device has processed since its last restart
     */
    private int slaveMessageCount = 0;
    /**
     * the quantity of CRC errors encountered by the remote device since its last restart
     */
    private int communicationErrorCount = 0;
    /**
     * the quantity of MODBUS exception responses returned by the remote device since its last restart
     */
    private int exceptionErrorCount = 0;
    /**
     * the quantity of messages addressed to the remote device for
     * which it has returned no response (neither a normal response nor an exception response),
     * since its last restart
     */
    private int slaveNoResponseCount = 0;
    /**
     * the quantity of messages addressed to the remote device for
     * which it returned a Negative Acknowledge (NAK) exception response, since its last restart
     */
    private int slaveNAKCount = 0;
    /**
     * the quantity of messages addressed to the remote device for
     * which it returned a Slave Device Busy exception response, since its last restart
     */
    private int slaveBusyCount = 0;
    /**
     * the quantity of messages addressed to the remote device that
     * it could not handle due to a character overrun condition, since its last restart
     */
    private int characterOverrunCount = 0;
    /**
     * Comm event queue. Capacity = PDU length(254) - server_address - function_code - 3 x 2 Bytes,
     * (Length of Status, Event Count and Message Count).
     */
    final private LinkedList<ModbusCommEvent> eventQueue = new LinkedList<ModbusCommEvent>();

    public CommStatus() {
    }

    public void enter() {
        commStatus = 0xffff;
    }

    public void leave() {
        commStatus = 0;
    }

    public void incEventCounter() {
        eventCount++;
    }

    public void incBusMessageCounter() {
        busMessageCount++;
    }

    public void incSlaveMessageCounter() {
        slaveMessageCount++;
    }

    public void incCommErrorCounter() {
        communicationErrorCount++;
    }

    public void incExErrorCounter() {
        exceptionErrorCount++;
    }

    public void incNoResponseCounter() {
        exceptionErrorCount++;
    }

    public void incSlaveNAKCounter() {
        exceptionErrorCount++;
    }

    public void incSlaveBusyCounter() {
        exceptionErrorCount++;
    }

    public void incCharacterOverrunCounter() {
        exceptionErrorCount++;
    }

    public int getCommStatus() {
        return commStatus;
    }

    public void setCommStatus(int commStatus) {
        this.commStatus = commStatus;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public int getMessageCount() {
        return busMessageCount;
    }

    public void setBusMessageCount(int busMessageCount) {
        this.busMessageCount = busMessageCount;
    }

    public int getSlaveMessageCount() {
        return slaveMessageCount;
    }

    public void setSlaveMessageCount(int slaveMessageCount) {
        this.slaveMessageCount = slaveMessageCount;
    }

    public int getCommunicationErrorCount() {
        return communicationErrorCount;
    }

    public void setCommunicationErrorCount(int communicationErrorCount) {
        this.communicationErrorCount = communicationErrorCount;
    }

    public int getExceptionErrorCount() {
        return exceptionErrorCount;
    }

    public void setExceptionErrorCount(int exceptionErrorCount) {
        this.exceptionErrorCount = exceptionErrorCount;
    }

    public int getSlaveNoResponseCount() {
        return slaveNoResponseCount;
    }

    public void setSlaveNoResponseCount(int slaveNoResponseCount) {
        this.slaveNoResponseCount = slaveNoResponseCount;
    }

    public int getSlaveNAKCount() {
        return slaveNAKCount;
    }

    public void setSlaveNAKCount(int slaveNAKCount) {
        this.slaveNAKCount = slaveNAKCount;
    }

    public int getSlaveBusyCount() {
        return slaveBusyCount;
    }

    public void setSlaveBusyCount(int slaveBusyCount) {
        this.slaveBusyCount = slaveBusyCount;
    }

    public int getCharacterOverrunCount() {
        return characterOverrunCount;
    }

    public void setCharacterOverrunCount(int characterOverrunCount) {
        this.characterOverrunCount = characterOverrunCount;
    }

    public int getDiagnosticRegister() {
        return diagnosticRegister;
    }

    public void setDiagnosticRegister(int diagnosticRegister) {
        this.diagnosticRegister = diagnosticRegister;
    }

    public void setAsciiInputDelimiter(int asciiInputDelimiter) {
        Modbus.setAsciiInputDelimiter(asciiInputDelimiter);
    }

    public boolean isListenOnlyMode() {
        return listenOnlyMode;
    }

    public void setListenOnlyMode(boolean listenOnlyMode) {
        this.listenOnlyMode = listenOnlyMode;
    }

    public boolean isRestartCommunicationsOption() {
        return restartCommunicationsOption;
    }

    public void setRestartCommunicationsOption(boolean restartCommunications) {
        this.restartCommunicationsOption = restartCommunications;
    }

    public boolean isClearLog() {
        return clearLog;
    }

    public void setClearLog(boolean clearLog) {
        this.clearLog = clearLog;
    }

    public void clearCountersAndDiagnosticRegister() {
        setBusMessageCount(0);
        setEventCount(0);
        setCharacterOverrunCount(0);
        setCommunicationErrorCount(0);
        setExceptionErrorCount(0);
        setSlaveNoResponseCount(0);
        setSlaveBusyCount(0);
        setSlaveMessageCount(0);
        setSlaveNAKCount(0);
    }

    public void restartCommunicationsOption() {
        clearCountersAndDiagnosticRegister();
        setListenOnlyMode(false);
        if (isClearLog()) {
            getEventLog().clear();
            setClearLog(false);
        }
    }

    public List<ModbusCommEvent> getEventLog() {
        return eventQueue;
    }

    public void setEventQueue(List<ModbusCommEvent> eventQueue) {
        this.eventQueue.clear();
        this.eventQueue.addAll(eventQueue);
    }

    public void addEvent(ModbusCommEvent event) {
        if (eventQueue.size() >= EVENT_QUEUE_CAPACITY)
            eventQueue.poll();
        eventQueue.add(event);
    }
}
