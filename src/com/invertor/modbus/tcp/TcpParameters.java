package com.invertor.modbus.tcp;

import com.invertor.modbus.Modbus;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
public class TcpParameters {
    private InetAddress host = null;
    private int port;
    private boolean keepAlive;

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
}
