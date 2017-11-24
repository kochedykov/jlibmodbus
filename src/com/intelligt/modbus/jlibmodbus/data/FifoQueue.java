package com.intelligt.modbus.jlibmodbus.data;

import com.intelligt.modbus.jlibmodbus.exception.IllegalDataValueException;

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
abstract public class FifoQueue {

    final private int capacity;

    public FifoQueue(int capacity) {
        this.capacity = capacity;
    }

    public abstract int size();

    abstract protected int[] peekImpl();

    abstract protected void addImpl(int register);

    abstract protected void pollImpl();

    synchronized final public void poll() {
        if (size() != 0)
            pollImpl();
    }

    synchronized final public void add(int register) {
        if (size() < capacity)
            addImpl(register);
    }

    synchronized final public int[] get() throws IllegalDataValueException {
        if (size() > 31 || size() == 0) {
            throw new IllegalDataValueException();
        }
        return peekImpl();
    }
}
