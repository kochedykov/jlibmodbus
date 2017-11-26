package com.intelligt.modbus.jlibmodbus.utils;

import java.net.InetAddress;

public class TcpClientInfo {
    final private InetAddress localAddress;
    final private int localPort;
    final private InetAddress remoteAddress;
    final private int remotePort;

    volatile private boolean isOpened;

    public TcpClientInfo(InetAddress localAddress, int localPort, InetAddress remoteAddress, int remotePort, boolean isOpened) {
        this.localAddress = localAddress;
        this.localPort = localPort;
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
        this.isOpened = isOpened;
    }

    public InetAddress getLocalAddress() {
        return localAddress;
    }

    public int getLocalPort() {
        return localPort;
    }

    public InetAddress getRemoteAddress() {
        return remoteAddress;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }
}
