package com.invertor.modbus.serial;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.net.stream.InputStreamTCP;
import com.invertor.modbus.net.stream.OutputStreamTCP;
import com.invertor.modbus.tcp.TcpParameters;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Copyright (c) 2015-2017 JLibModbus developers team
 * <p/>
 * [http://jlibmodbus.sourceforge.net]
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

public class SerialPortFactoryTcp implements SerialPortAbstractFactory {

    private TcpParameters tcpParameters;

    public SerialPortFactoryTcp(TcpParameters tcpParameters) {
        setTcpParameters(tcpParameters);
    }

    public TcpParameters getTcpParameters() {
        return tcpParameters;
    }

    public void setTcpParameters(TcpParameters tcpParameters) {
        this.tcpParameters = tcpParameters;
    }

    public SerialPort createSerial(SerialParameters sp) {
        return new SerialPortViaTCP(sp);
    }

    private class SerialPortViaTCP extends SerialPort {

        private Socket socket;
        private InputStreamTCP in;
        private OutputStreamTCP os;

        public SerialPortViaTCP(SerialParameters sp) {
            super(sp);
        }

        @Override
        public void purgeRx() {
            //do nothing
        }

        @Override
        public void purgeTx() {
            //do nothing
        }

        @Override
        public void open() throws SerialPortException {
            TcpParameters parameters = getTcpParameters();
            if (parameters != null) {
                close();
                socket = new Socket();
                InetSocketAddress isa = new InetSocketAddress(parameters.getHost(), parameters.getPort());
                try {
                    socket.connect(isa, Modbus.MAX_CONNECTION_TIMEOUT);
                    socket.setKeepAlive(parameters.isKeepAlive());

                    socket.setSoTimeout(Modbus.MAX_RESPONSE_TIMEOUT);

                    in = new InputStreamTCP(socket);
                    os = new OutputStreamTCP(socket);
                } catch (Exception e) {
                    throw new SerialPortException(e);
                }
            }
        }

        @Override
        public void write(byte[] b) throws IOException {
            if (isOpened()) {
                os.write(b);
                os.flush();
            } else {
                throw new IOException("Port not opened");
            }
        }

        @Override
        public void write(int b) throws IOException {
            if (isOpened()) {
                os.write(b);
                os.flush();
            } else {
                throw new IOException("Port not opened");
            }
        }

        @Override
        public int read() throws IOException {
            if (isOpened()) {
                return in.read();
            } else {
                throw new IOException("Port not opened");
            }
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (isOpened()) {
                return in.read(b, off, len);
            } else {
                throw new IOException("Port not opened");
            }
        }

        @Override
        public void close() {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (in != null) {
                    in.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                //do nothing
            } finally {
                socket = null;
                os = null;
                in = null;
            }
        }

        public void setReadTimeout(int readTimeout) {
            super.setReadTimeout(readTimeout);
            try {
                socket.setSoTimeout(readTimeout);
            } catch (Exception e) {
                //do nothing
            }
        }

        @Override
        public boolean isOpened() {
            return socket.isConnected();
        }
    }
}
