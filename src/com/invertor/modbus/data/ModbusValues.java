package com.invertor.modbus.data;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.exception.IllegalDataAddressException;
import com.invertor.modbus.exception.IllegalDataValueException;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Observable;

abstract public class ModbusValues<T> extends Observable implements Iterable<T> {

    abstract public int getQuantity();

    abstract public T get(int offset) throws IllegalDataAddressException;

    abstract public void setImpl(int offset, T value) throws IllegalDataAddressException, IllegalDataValueException;

    abstract public int getByteCount();

    abstract public byte[] getBytes();

    abstract public void setBytesBe(byte[] bytes);

    final public void set(int offset, T value) throws IllegalDataAddressException, IllegalDataValueException {
        setImpl(offset, value);
        notifyObservers(new int[]{offset, 1});
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                if (index >= getQuantity())
                    throw new ConcurrentModificationException();
                return (index + 1) < getQuantity();
            }

            @Override
            public T next() {
                if (hasNext()) {
                    try {
                        return get(index++);
                    } catch (IllegalDataAddressException e) {
                        Modbus.log().severe(this.getClass().getSimpleName() + " " + e.getLocalizedMessage() + ": quantity = " + getQuantity() + ", index = " + index);
                        throw new NoSuchElementException();
                    }
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {

            }
        };
    }
}
