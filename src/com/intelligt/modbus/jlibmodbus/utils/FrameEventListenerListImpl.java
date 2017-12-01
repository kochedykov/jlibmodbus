package com.intelligt.modbus.jlibmodbus.utils;

import java.util.ArrayList;
import java.util.List;
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
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

/**
 * @author kochedykov
 * @since 1.9.0
 */
public class FrameEventListenerListImpl implements FrameEventListenerList {
    private final List<FrameEventListener> eventListenerList = new ArrayList<FrameEventListener>();

    public FrameEventListenerListImpl() {

    }

    public void addListener(FrameEventListener listener) {
        synchronized (this) {
            eventListenerList.add(listener);
        }
    }

    public void removeListener(FrameEventListener listener) {
        synchronized (this) {
            eventListenerList.remove(listener);
        }
    }

    @Override
    public void removeListeners() {
        eventListenerList.clear();
    }

    public void fireFrameReceivedEvent(FrameEvent event) {
        for (FrameEventListener l : eventListenerList) {
            l.frameReceivedEvent(event);
        }
    }

    public void fireFrameSentEvent(FrameEvent event) {
        for (FrameEventListener l : eventListenerList) {
            l.frameSentEvent(event);
        }
    }

    @Override
    public int countListeners() {
        return eventListenerList.size();
    }
}
