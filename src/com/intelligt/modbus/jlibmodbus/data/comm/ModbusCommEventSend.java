package com.intelligt.modbus.jlibmodbus.data.comm;

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
public class ModbusCommEventSend extends ModbusCommEvent {
    final static private int BIT_READ_EXCEPTION_SENT = 0x1;
    final static private int BIT_SLAVE_ABORT_EXCEPTION_SENT = 0x2;
    final static private int BIT_SLAVE_BUSY_EXCEPTION_SENT = 0x4;
    final static private int BIT_SLAVE_PROGRAM_NAK_EXCEPTION_SENT = 0x8;
    final static private int BIT_WRITE_TIMEOUT_ERROR_OCCURRED = 0x10;
    final static private int BIT_CURRENTLY_IN_LISTEN_ONLY_MODE = 0x20;

    protected ModbusCommEventSend(int event) {
        super(Type.SEND, event);
    }

    static public ModbusCommEventSend createExceptionSentRead() {
        return new ModbusCommEventSend(BIT_READ_EXCEPTION_SENT);
    }

    static public ModbusCommEventSend createExceptionSlaveSentAbort() {
        return new ModbusCommEventSend(BIT_SLAVE_ABORT_EXCEPTION_SENT);
    }

    static public ModbusCommEventSend createExceptionSlaveSentBusy() {
        return new ModbusCommEventSend(BIT_SLAVE_BUSY_EXCEPTION_SENT);
    }

    static public ModbusCommEventSend createExceptionSentSlaveProgramNAK() {
        return new ModbusCommEventSend(BIT_SLAVE_PROGRAM_NAK_EXCEPTION_SENT);
    }

    static public ModbusCommEventSend createWriteTimeoutErrorOccurred() {
        return new ModbusCommEventSend(BIT_WRITE_TIMEOUT_ERROR_OCCURRED);
    }

    static public ModbusCommEventSend createCurrentlyInListenOnlyMode() {
        return new ModbusCommEventSend(BIT_CURRENTLY_IN_LISTEN_ONLY_MODE);
    }

    public boolean isCurrentlyInListenOnlyMode() {
        return isBitsSet(BIT_CURRENTLY_IN_LISTEN_ONLY_MODE);
    }

    public boolean isExceptionSentRead() {
        return isBitsSet(BIT_READ_EXCEPTION_SENT);
    }

    public boolean isExceptionSlaveSentAbort() {
        return isBitsSet(BIT_SLAVE_ABORT_EXCEPTION_SENT);
    }

    public boolean isExceptionSlaveSentBusy() {
        return isBitsSet(BIT_SLAVE_BUSY_EXCEPTION_SENT);
    }

    public boolean isExceptionSentSlaveProgramNAK() {
        return isBitsSet(BIT_SLAVE_PROGRAM_NAK_EXCEPTION_SENT);
    }

    public boolean isWriteTimeoutErrorOccurred() {
        return isBitsSet(BIT_WRITE_TIMEOUT_ERROR_OCCURRED);
    }
}