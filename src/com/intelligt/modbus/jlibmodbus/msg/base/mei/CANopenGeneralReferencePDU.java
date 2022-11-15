package com.intelligt.modbus.jlibmodbus.msg.base.mei;

import com.intelligt.modbus.jlibmodbus.data.DataHolder;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusInputStream;
import com.intelligt.modbus.jlibmodbus.net.stream.base.ModbusOutputStream;
import com.intelligt.modbus.jlibmodbus.utils.*;

import java.io.IOException;
import static java.lang.Math.min;

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
 * Author: Jan Schloessin
 * created 2022-08-12
 */
public class CANopenGeneralReferencePDU implements ModbusEncapsulatedInterface {

    private short objectIndex = 0;
    private byte subIndex = 0;
    private byte byteCount = 0;
    private int responseSize = 0;
    private boolean writeRequest = false;
    private byte protocolOptions = 0;
    private byte peerNodeId = 0;
    private short startingAddress = 0;
    private byte sdoObject = 0;
    private final byte[] data = new byte[4];

    @Override
    public MEITypeCode getTypeCode() {
        return MEITypeCode.CAN_OPEN_PDU;
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        fifo.write(writeRequest ? 1 : 0);
        fifo.write(protocolOptions);
        fifo.write(peerNodeId);
        fifo.write(DataUtils.toByteArray(objectIndex));
        fifo.write(subIndex);
        fifo.write(DataUtils.toByteArray(startingAddress));
        fifo.write(sdoObject);
        fifo.write(byteCount);
        if (writeRequest)
          fifo.write(data, 0, min(byteCount, 4));
    }

    @Override
    public void readRequest(ModbusInputStream fifo) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getRequestSize() {
        if (writeRequest)
          return 10 + byteCount;

        return 10;
    }

    @Override
    public void writeResponse(ModbusOutputStream fifo) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        writeRequest = fifo.read() != 0;
        protocolOptions = (byte) fifo.read();
        peerNodeId = (byte) fifo.read();
        objectIndex = (short) fifo.readShortBE();
        subIndex = (byte) fifo.read();
        startingAddress = (short) fifo.readShortBE();
        sdoObject = (byte) fifo.read();
        byteCount = (byte) fifo.read();
        for (int i = 0; i < min(byteCount, data.length); i++)
          data[i] = (byte) fifo.read();
        for (int i = byteCount; i < data.length; i++)
          data[i] = 0;
    }

    @Override
    public int getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(int responseSize) {
        this.responseSize = responseSize;
    }

    @Override
    public void process(DataHolder dataHolder) {
        throw new UnsupportedOperationException();
    }

    public boolean isWriteRequest() {
      return writeRequest;
    }

    public void setWriteRequest(boolean writeRequest) {
      this.writeRequest = writeRequest;
    }

    public short getObjectIndex() {
        return objectIndex;
    }

    public void setObjectIndex(short objectIndex) {
        this.objectIndex = objectIndex;
    }

    public byte getSubIndex() {
        return subIndex;
    }

    public void setSubIndex(byte subIndex) {
        this.subIndex = subIndex;
    }

    public byte getByteCount() {
        return byteCount;
    }

    public void setByteCount(byte byteCount) {
        this.byteCount = byteCount;
    }

  public byte getPeerNodeId() {
    return peerNodeId;
  }

  public void setPeerNodeId(byte nodeId) {
    this.peerNodeId = nodeId;
  }

  public byte asUInt8() {
    return data[0];
  }

  public short asUInt16() {
    return (short) (
             data[0]
            | (data[1] << 8));
  }

  public int asUInt32() {
    return data[0]
            | (data[1] << 8)
            | (data[2] << 16)
            | (data[3] << 24);
  }

  public void send(int data) {
    this.data[0] = (byte) (data & 0xffff);
    this.data[1] = (byte) (data >> 8 & 0xffff);
    this.data[2] = (byte) (data >> 16 & 0xffff);
    this.data[3] = (byte) (data >> 24 & 0xffff);
  }

}
