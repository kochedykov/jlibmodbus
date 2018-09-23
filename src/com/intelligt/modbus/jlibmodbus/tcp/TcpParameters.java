package com.intelligt.modbus.jlibmodbus.tcp;

import com.intelligt.modbus.jlibmodbus.Modbus;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
public class TcpParameters {
    private InetAddress host = null;
    private int port;
    private boolean keepAlive;
    private int connectionTimeout = Modbus.MAX_CONNECTION_TIMEOUT;

    public TcpParameters() {
        try {
            setHost(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setPort(Modbus.TCP_PORT);
        setKeepAlive(true);
    }

    public TcpParameters(TcpParameters p) {
        this(p.getHost(), p.getPort(), p.isKeepAlive());
        setConnectionTimeout(p.getConnectionTimeout());
    }

    public TcpParameters(InetAddress host, int port, boolean keepAlive) {
        setHost(host);
        setPort(port);
        setKeepAlive(keepAlive);
    }

    public TcpParameters(String host, int port, boolean keepAlive) {
        try {
            setHost(InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setPort(port);
        setKeepAlive(keepAlive);
    }

    public InetAddress getHost() {
        return host;
    }

    public void setHost(InetAddress host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
    
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
