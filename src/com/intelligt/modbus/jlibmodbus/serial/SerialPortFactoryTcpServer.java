package com.intelligt.modbus.jlibmodbus.serial;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.net.stream.InputStreamTCP;
import com.intelligt.modbus.jlibmodbus.net.stream.OutputStreamTCP;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
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

public class SerialPortFactoryTcpServer extends SerialPortAbstractFactory {

    final private TcpParameters tcpParameters;

    public SerialPortFactoryTcpServer(TcpParameters tcpParameters) {
        this.tcpParameters = tcpParameters;
    }

    public TcpParameters getTcpParameters() {
        return tcpParameters;
    }

    @Override
    public SerialPort createSerialImpl(SerialParameters sp) {
        return new SerialPortTcpServer(sp);
    }

    @Override
    public List<String> getPortIdentifiersImpl() {
        return new LinkedList<String>();
    }

    class SerialPortTcpServer extends SerialPort implements Runnable {
        volatile boolean listening = false;
        private Thread mainThread = null;
        private ServerSocket server = null;
        private Socket client = null;
        private InputStreamTCP is = null;
        private OutputStreamTCP os = null;
        private int timeout = 1000;

        SerialPortTcpServer(SerialParameters sp) {
            super(sp);
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        private boolean isListening() {
            return listening;
        }

        private void setListening(boolean listening) {
            this.listening = listening;
        }

        @Override
        public void open() throws SerialPortException {
            try {
                server = new ServerSocket(getTcpParameters().getPort());
                mainThread = new Thread(this);
                setListening(true);
                mainThread.start();
            } catch (IOException e) {
                throw new SerialPortException(e);
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
            setListening(false);
            /* close server socket */
            try {
                if (server != null)
                    server.close();
            } catch (IOException e) {
                Modbus.log().warning(e.getLocalizedMessage());
            } finally {
                server = null;
            }

            try {
                if (mainThread != null) {
                    mainThread.join(timeout * 2);
                    if (mainThread.isAlive())
                        mainThread.interrupt();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mainThread = null;
            }
        }

        public void setReadTimeout(int readTimeout) {
            setTimeout(readTimeout);

            synchronized (this) {
                if (isOpened()) {
                    super.setReadTimeout(readTimeout);
                    try {
                        client.setSoTimeout(readTimeout);
                    } catch (SocketException e) {
                        e.printStackTrace();
                        Modbus.log().warning("Unable to set readTimeout: " + e.getLocalizedMessage());
                    }
                }
            }
        }

        @Override
        public boolean isOpened() {
            return client != null && client.isConnected() && os != null && is != null;
        }

        @Override
        public void run() {
            try {
                while (isListening()) {
                    client = server.accept();
                    is = new InputStreamTCP(client);
                    os = new OutputStreamTCP(client);
                    while (client.isConnected() && isListening()) {
                        Thread.sleep(timeout);
                    }
                }
            } catch (SocketException e) {
                if (server != null && !server.isClosed()) {
                    Modbus.log().warning(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
