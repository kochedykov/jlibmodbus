package com.invertor.modbus.data;

import com.invertor.modbus.exception.IllegalDataAddressException;
import com.invertor.modbus.exception.IllegalDataValueException;

import java.util.Observable;

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

/**
 * since 1.2.8.4 it extends Observable to notify observers if register values was changed.
 *
 * @see java.util.Observable
 * @see java.util.Observer
 */
public abstract class Coils extends Observable {

    abstract public int quantity();

    abstract public boolean get(int offset) throws IllegalDataAddressException;

    abstract public boolean[] getRange(int offset, int quantity) throws IllegalDataAddressException, IllegalDataValueException;

    public void set(int offset, boolean value) throws IllegalDataAddressException {
        notifyObservers(new int[]{offset, 1});
    }

    public void setRange(int offset, boolean[] range) throws IllegalDataAddressException, IllegalDataValueException {
        notifyObservers(new int[]{offset, range.length});
    }
}
