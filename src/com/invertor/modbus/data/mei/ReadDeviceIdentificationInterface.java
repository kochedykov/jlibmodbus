package com.invertor.modbus.data.mei;

import com.invertor.modbus.exception.IllegalDataAddressException;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;

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
public class ReadDeviceIdentificationInterface {

    final static public int BASIC_OBJECT_COUNT = 3;
    final static public int REGULAR_OBJECT_COUNT = 0x7D;
    final static public int EXTENDED_OBJECT_COUNT = 0x7F;
    final static public int OBJECT_ID_VENDOR_NAME = 0;
    final static public int OBJECT_ID_PRODUCT_CODE = 1;
    final static public int OBJECT_ID_MAJOR_MINOR_REVISION = 2;
    final static public int OBJECT_ID_VENDOR_URL = 3;
    final static public int OBJECT_ID_PRODUCT_NAME = 4;
    final static public int OBJECT_ID_MODEL_NAME = 5;
    final static public int OBJECT_ID_USER_APPLICATION_NAME = 6;
    final private DataObjectsHolder dataObjectsBasic = new DataObjectsHolder(BASIC_OBJECT_COUNT);
    final private DataObjectsHolder dataObjectsRegular = new DataObjectsHolder(REGULAR_OBJECT_COUNT);
    final private DataObjectsHolder dataObjectsExtended = new DataObjectsHolder(EXTENDED_OBJECT_COUNT);

    public ReadDeviceIdentificationInterface() {
        for (int i = 0; i < BASIC_OBJECT_COUNT; i++)
            dataObjectsBasic.add(new DataObject(i, new byte[0]));
    }

    DataObjectsHolder getDataObjectsHolder(int id) {
        if (id < 0)
            throw new IllegalArgumentException();
        if (id >= BASIC_OBJECT_COUNT)
            return dataObjectsRegular;
        if (id >= REGULAR_OBJECT_COUNT)
            return dataObjectsExtended;
        return dataObjectsBasic;
    }

    public void setValue(int id, String value) {
        setValue(new DataObject(id, value.getBytes()));
    }

    public void setValue(DataObject dataObject) {
        getDataObjectsHolder(dataObject.getId()).add(dataObject);
    }

    public DataObject getValue(int id) {
        return getDataObjectsHolder(id).get(id);
    }

    public DataObject getVendorName() {
        return getValue(OBJECT_ID_VENDOR_NAME);
    }

    public void setVendorName(String value) {
        setValue(OBJECT_ID_VENDOR_NAME, value);
    }

    public DataObject getProductCode() {
        return getValue(OBJECT_ID_PRODUCT_CODE);
    }

    public void setProductCode(String value) {
        setValue(OBJECT_ID_PRODUCT_CODE, value);
    }

    public DataObject getMajorMinorRevision() {
        return getValue(OBJECT_ID_MAJOR_MINOR_REVISION);
    }

    public void setMajorMinorRevision(String value) {
        setValue(OBJECT_ID_MAJOR_MINOR_REVISION, value);
    }

    public DataObject getVendorUrl() throws IllegalDataAddressException {
        return getValue(OBJECT_ID_VENDOR_URL);
    }

    public void setVendorUrl(String value) {
        setValue(OBJECT_ID_VENDOR_URL, value);
    }

    public DataObject getProductName() throws IllegalDataAddressException {
        return getValue(OBJECT_ID_PRODUCT_NAME);
    }

    public void setProductName(String value) {
        setValue(OBJECT_ID_PRODUCT_NAME, value);
    }

    public DataObject getModelName() throws IllegalDataAddressException {
        return getValue(OBJECT_ID_MODEL_NAME);
    }

    public void setModelName(String value) {
        setValue(OBJECT_ID_MODEL_NAME, value);
    }

    public DataObject getUserApplicationName() throws IllegalDataAddressException {
        return getValue(OBJECT_ID_USER_APPLICATION_NAME);
    }

    public void setUserApplicationName(String value) {
        setValue(OBJECT_ID_USER_APPLICATION_NAME, value);
    }

    public DataObject[] getBasic() throws IllegalDataAddressException {
        return dataObjectsBasic.toArray();
    }

    public DataObject[] getRegular() throws IllegalDataAddressException {
        return dataObjectsRegular.toArray();
    }

    public DataObject[] getExtended() throws IllegalDataAddressException {
        return dataObjectsExtended.toArray();
    }

    static public class DataObject {
        final private int id;
        private byte[] value;

        public DataObject(int id, byte[] value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public byte[] getValue() {
            return value;
        }

        public void setValue(byte[] value) {
            this.value = Arrays.copyOf(value, value.length);
        }
    }

    private class DataObjectsHolder {
        final private int capacity;
        TreeMap<Integer, DataObject> map;

        public DataObjectsHolder(int capacity) {
            this.capacity = capacity;
            map = new TreeMap<Integer, DataObject>();
        }

        public void add(DataObject dataObject) {
            if (map.containsKey(dataObject.getId())) {
                map.get(dataObject.getId()).setValue(dataObject.getValue());
            } else if (map.size() < capacity) {
                map.put(dataObject.getId(), dataObject);
            }
        }

        public DataObject get(int id) {
            return map.get(id);
        }

        public DataObject[] toArray() {
            return map.values().toArray(new DataObject[map.size()]);
        }
    }
}
