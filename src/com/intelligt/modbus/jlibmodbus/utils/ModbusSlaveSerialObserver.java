package com.intelligt.modbus.jlibmodbus.utils;

import java.util.Observable;
import java.util.Observer;

abstract public class ModbusSlaveSerialObserver implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof SerialPortInfo) {
            SerialPortInfo serialPortInfo = (SerialPortInfo) arg;
            if (serialPortInfo.isOpened()) {
                clientAccepted(serialPortInfo);
            } else {
                clientDisconnected(serialPortInfo);
            }
        }
    }

    public abstract void clientAccepted(SerialPortInfo info);

    public abstract void clientDisconnected(SerialPortInfo info);
}
