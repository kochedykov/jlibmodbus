package com.intelligt.modbus.jlibmodbus.utils;

import java.util.Observable;
import java.util.Observer;

abstract public class ModbusSlaveTcpObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof TcpClientInfo) {
            TcpClientInfo clientInfo = (TcpClientInfo) arg;
            if (clientInfo.isOpened()) {
                clientAccepted(clientInfo);
            } else {
                clientDisconnected(clientInfo);
            }
        }
    }

    abstract void clientAccepted(TcpClientInfo info);

    abstract void clientDisconnected(TcpClientInfo info);
}
