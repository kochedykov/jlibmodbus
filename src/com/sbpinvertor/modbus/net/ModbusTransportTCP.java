package com.sbpinvertor.modbus.net;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.data.ModbusInputStream;
import com.sbpinvertor.modbus.data.ModbusOutputStream;
import com.sbpinvertor.modbus.data.base.ModbusMessage;
import com.sbpinvertor.modbus.data.base.ModbusResponse;
import com.sbpinvertor.modbus.exception.ModbusTransportException;
import com.sbpinvertor.modbus.utils.DataUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Copyright (c) 2015-2016 JSC "Zavod "Invertor"
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
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
final public class ModbusTransportTCP extends ModbusTransport {

    final private String host;
    final private int port;
    final private boolean keepAlive;
    final private AduHeader headerIn = new AduHeader();
    final private AduHeader headerOut = new AduHeader();
    private final byte[] pdu = new byte[Modbus.MAX_PDU_LENGTH];
    private Socket socket;
    private TcpSocketInputStream is;
    private TcpSocketOutputStream os;

    public ModbusTransportTCP(String host, int port, boolean keepAlive) {
        this.host = host;
        this.port = port;
        this.keepAlive = keepAlive;

        try {
            if (keepAlive)
                openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModbusTransportTCP(String host, boolean keepAlive) {
        this(host, Modbus.TCP_PORT, keepAlive);
    }

    public ModbusTransportTCP(String host) {
        this(host, Modbus.TCP_PORT, false);
    }

    private void sendAdu(ModbusMessage msg) throws ModbusTransportException, IOException {
        // modbus tcp adu header
        os.write(headerOut.update(msg.size()));
        super.send(msg);
        if (is.read(headerIn.byteArray(), 0, AduHeader.SIZE) < AduHeader.SIZE) {
            throw new ModbusTransportException("Error: no response.");
        }
        if (headerIn.getPduSize() > Modbus.MAX_TCP_ADU_LENGTH) {
            throw new ModbusTransportException("Maximum ADU size is reached.");
        }
    }

    @Override
    protected void send(ModbusMessage msg) throws ModbusTransportException {
        if (!keepAlive)
            openConnection();
        try {
            try {
                sendAdu(msg);
            } catch (Exception e) {
                if (keepAlive) {
                    openConnection();
                    sendAdu(msg);
                } else {
                    throw e;
                }
            }
        } catch (Exception e) {
            throw new ModbusTransportException(e);
        }
    }

    @Override
    public ModbusResponse sendRequest(ModbusMessage msg) throws ModbusTransportException {
        try {
            return super.sendRequest(msg);
        } finally {
            if (!keepAlive)
                closeConnection();
        }
    }

    synchronized private void openConnection() throws ModbusTransportException {
        closeConnection();
        try {
            socket = new Socket();
            socket.setKeepAlive(keepAlive);
            socket.setSoTimeout(getResponseTimeout());
            socket.connect(new InetSocketAddress(host, port), Modbus.MAX_CONNECTION_TIMEOUT);
            is = new TcpSocketInputStream(socket.getInputStream());
            os = new TcpSocketOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            closeConnection();
            throw new ModbusTransportException(e);
        }
    }

    synchronized private void closeConnection() throws ModbusTransportException {
        try {
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            throw new ModbusTransportException(e);
        } finally {
            is = null;
            os = null;
            socket = null;
        }
    }

    @Override
    public ModbusOutputStream getOutputStream() {
        return os;
    }

    @Override
    public ModbusInputStream getInputStream() {
        return is;
    }

    final static private class AduHeader {
        final static public int SIZE = 6;
        final private byte[] buffer;

        public AduHeader() {
            buffer = new byte[6];
            setProtocolId(Modbus.PROTOCOL_ID);
        }

        public void setBufferValue(int value, int offset) {
            buffer[offset++] = DataUtils.byteHigh(value);
            buffer[offset] = DataUtils.byteLow(value);
        }

        public short getPduSize() {
            return DataUtils.toShort(buffer[4], buffer[5]);
        }

        public void setPduSize(int value) {
            setBufferValue(value, 4);
        }

        public short getProtocolId() {
            return DataUtils.toShort(buffer[2], buffer[3]);
        }

        public void setProtocolId(int value) {
            setBufferValue(value, 2);
        }

        public short getTransactionId() {
            return DataUtils.toShort(buffer[0], buffer[1]);
        }

        public void setTransactionId(int value) {
            setBufferValue(value, 0);
        }

        public void update(byte[] header) {
            System.arraycopy(header, 0, buffer, 0, buffer.length);
        }

        public byte[] byteArray() {
            return buffer;
        }

        private byte[] update(int pduSize) {
            //transaction id (2 bytes, BE)
            setTransactionId(getTransactionId() + 1);
            //size of PDU (2 bytes, BE)
            setPduSize(pduSize);
            return buffer;
        }
    }

    private class TcpSocketInputStream extends ModbusInputStream {

        volatile private BufferedInputStream is;

        protected TcpSocketInputStream(InputStream is) {
            this.is = new BufferedInputStream(is);
        }

        @Override
        public int read() throws IOException {
            int c = is.read();
            if (c == -1) {
                c = is.read();
            }
            return c;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int count = 0;
            int k = 0;
            while (count < len && k != -1) {
                k = is.read(b, off + count, len - count);
                if (-1 != k)
                    count += k;
            }
            return count;
        }
    }

    private class TcpSocketOutputStream extends ModbusOutputStream {

        volatile private BufferedOutputStream os;

        protected TcpSocketOutputStream(OutputStream os) {
            this.os = new BufferedOutputStream(os);
        }

        @Override
        public void write(byte[] b) throws IOException {
            os.write(b);
        }

        @Override
        public void write(int b) throws IOException {
            os.write(b);
        }

        @Override
        public void flush() throws IOException {
            os.flush();
        }
    }
}
