package com.intelligt.modbus.jlibmodbus.serial;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.net.stream.InputStreamTCP;
import com.intelligt.modbus.jlibmodbus.net.stream.OutputStreamTCP;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

/*
 * Copyright (C) 2017 Vladislav Y. Kochedykov
 *
 * [http://jlibmodbus.sourceforge.net]
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
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */

public class SerialPortFactoryTcpClient extends SerialPortAbstractFactory {

    private TcpParameters tcpParameters;

    public SerialPortFactoryTcpClient(TcpParameters tcpParameters) {
        setTcpParameters(tcpParameters);
    }

    public TcpParameters getTcpParameters() {
        return tcpParameters;
    }

    public void setTcpParameters(TcpParameters tcpParameters) {
        this.tcpParameters = tcpParameters;
    }

    @Override
    public SerialPort createSerialImpl(SerialParameters sp) {
        return new SerialPortTcpClient(sp);
    }

    @Override
    public List<String> getPortIdentifiersImpl() {
        return Arrays.asList(new String[0]);
    }

    private class SerialPortTcpClient extends SerialPort {

        private Socket socket;
        private InputStreamTCP is;
        private OutputStreamTCP os;

        SerialPortTcpClient(SerialParameters sp) {
            super(sp);
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
                    socket.setSoTimeout(getReadTimeout());

                    is = new InputStreamTCP(socket);
                    os = new OutputStreamTCP(socket);

                } catch (SocketException e) {
                    throw new SerialPortException(e);
                } catch (IOException e) {
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
                return is.read();
            } else {
                throw new IOException("Port not opened");
            }
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (isOpened()) {
                return is.read(b, off, len);
            } else {
                throw new IOException("Port not opened");
            }
        }

        @Override
        public void close() {
            try {
                if (isOpened()) {
                    is.close();
                    os.close();
                    socket.close();
                }
            } catch (IOException e) {
                Modbus.log().warning("Unable to close port: " + e.getLocalizedMessage());
            } finally {
                is = null;
                os = null;
                socket = null;
            }
        }

        public void setReadTimeout(int readTimeout) {
            super.setReadTimeout(readTimeout);
            if (isOpened()) {
                try {
                    socket.setSoTimeout(readTimeout);
                } catch (SocketException e) {
                    e.printStackTrace();
                    Modbus.log().warning("Unable to set readTimeout: " + e.getLocalizedMessage());
                }
            }
        }

        @Override
        public boolean isOpened() {
            return socket != null && socket.isConnected() && os != null && is != null;
        }
    }
}
