package com.invertor.modbus.serial;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/*
 * Copyright (C) 2017 "Invertor" Factory", JSC
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
 * Authors: Kevin Kieffer.
 * email: <kkieffer@adaptivemethods.com>
 */

/**
 * @author Kevin Kieffer
 * @since 1.9.0
 */
public class SerialPortLoopback extends SerialPort {

    //This must be static to be used by all class instances
    private static final LinkedBlockingDeque<Byte> tx_from_master_fifo = new LinkedBlockingDeque<Byte>(8192);
    private static final LinkedBlockingDeque<Byte> rx_to_master_fifo = new LinkedBlockingDeque<Byte>(8192);
    private final LinkedBlockingDeque<Byte> myWriteFifo;
    private final LinkedBlockingDeque<Byte> myReadFifo;
    private boolean isOpen = false;

    public SerialPortLoopback(SerialParameters sp, boolean isMaster) throws SerialPortException {
        super(sp);
        if (isMaster) {
            myWriteFifo = tx_from_master_fifo;
            myReadFifo = rx_to_master_fifo;
        } else {
            myWriteFifo = rx_to_master_fifo;
            myReadFifo = tx_from_master_fifo;
        }
    }

    @Override
    public void write(int b) throws IOException {
        if (!myWriteFifo.offer((byte) b))
            throw new IOException("Loopback fifo full");
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        if (!isOpen)
            throw new IOException("Loopback is not open");
        for (Byte b : bytes) {
            if (!myWriteFifo.offer(b))
                throw new IOException("Loopback fifo is full");
        }
    }

    @Override
    public void open() throws SerialPortException {
        isOpen = true;
    }

    @Override
    public int read() throws IOException {
        if (!isOpen)
            throw new IOException("Loopback is not open");

        try {

            Byte b = myReadFifo.poll((long) getReadTimeout() * 5000, TimeUnit.MILLISECONDS);
            if (b == null)
                throw new IOException("Timeout");

            return (int) b & 0xff;

        } catch (InterruptedException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {

        int count = 0;
        for (int i = 0; i < len; i++) {
            try {
                Byte y = myReadFifo.poll((long) getReadTimeout(), TimeUnit.MILLISECONDS);
                if (y == null)
                    break;
                b[i + off] = y;
                count++;

            } catch (InterruptedException ex) {
                throw new IOException(ex);
            }
        }
        if (count == 0)
            throw new IOException("Timeout");

        return count;
    }

    @Override
    public void close() {
        isOpen = false;
    }

    @Override
    public boolean isOpened() {
        return isOpen;
    }
}
