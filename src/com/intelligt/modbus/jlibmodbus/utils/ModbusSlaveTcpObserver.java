package com.intelligt.modbus.jlibmodbus.utils;

import java.util.Observable;
import java.util.Observer;

abstract public class ModbusSlaveTcpObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof TcpClientInfo) {
            TcpClientInfo clientInfo = (TcpClientInfo) arg;
            if (clientInfo.isConnected()) {
                clientAccepted(clientInfo);
            } else {
                clientDisconnected(clientInfo);
            }
        }
    }

    public abstract void clientAccepted(TcpClientInfo info);

    public abstract void clientDisconnected(TcpClientInfo info);
}
