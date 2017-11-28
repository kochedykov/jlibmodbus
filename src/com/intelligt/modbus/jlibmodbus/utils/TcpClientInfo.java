package com.intelligt.modbus.jlibmodbus.utils;

import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

public class TcpClientInfo {
    private TcpParameters tcpParameters = new TcpParameters();

    volatile private boolean connected;

    public TcpClientInfo(TcpParameters tcpParameters, boolean connected) {
        this.tcpParameters = tcpParameters;
        this.connected = connected;
    }

    public TcpParameters getTcpParameters() {
        return tcpParameters;
    }

    public void setTcpParameters(TcpParameters tcpParameters) {
        this.tcpParameters = tcpParameters;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
