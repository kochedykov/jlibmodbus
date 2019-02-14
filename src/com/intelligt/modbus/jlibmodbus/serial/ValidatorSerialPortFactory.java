package com.intelligt.modbus.jlibmodbus.serial;

import com.intelligt.modbus.jlibmodbus.Modbus;

import java.lang.reflect.InvocationTargetException;

public class ValidatorSerialPortFactory implements Comparable<ValidatorSerialPortFactory> {
    final private String factoryClassname;
    final private String connectorClassname;

    public ValidatorSerialPortFactory(String connectorClassname, String factoryClassname) {
        this.factoryClassname = factoryClassname;
        this.connectorClassname = connectorClassname;
    }

    public String getFactoryClassname() {
        return factoryClassname;
    }

    public String getConnectorClassname() {
        return connectorClassname;
    }

    public boolean validate() {
        try {
            Class.forName(getConnectorClassname());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public SerialPortAbstractFactory getFactory() throws ClassNotFoundException {
        if (validate()) {
            try {
                Class factoryClass = Class.forName(getFactoryClassname());
                Object o = factoryClass.getConstructors()[0].newInstance();
                if (o instanceof SerialPortAbstractFactory) {
                    return (SerialPortAbstractFactory) o;
                }
            } catch (Exception e) {
                Modbus.log().warning("Invalid implementation of " + getFactoryClassname());
            }
        }
        throw new ClassNotFoundException();
    }

    @Override
    public int compareTo(ValidatorSerialPortFactory o) {
        return this.equals(o) ? 0 : 1;
    }
}
