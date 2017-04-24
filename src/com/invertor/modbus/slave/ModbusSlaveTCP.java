package com.invertor.modbus.slave;

import com.invertor.modbus.Modbus;
import com.invertor.modbus.ModbusSlave;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.net.ModbusConnection;
import com.invertor.modbus.net.ModbusConnectionFactory;
import com.invertor.modbus.tcp.TcpParameters;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ModbusSlaveTCP extends ModbusSlave implements Runnable {

    final private static int DEFAULT_POOLS_SIZE = 10;
    final private ExecutorService threadPool;
    final private TcpParameters tcp;
    private Thread mainThread = null;
    private ServerSocket server = null;

    public ModbusSlaveTCP(TcpParameters tcp) {
        this(tcp, DEFAULT_POOLS_SIZE);
    }

    public ModbusSlaveTCP(TcpParameters tcp, int poolsSize) {
        this.tcp = new TcpParameters(tcp);
        threadPool = Executors.newFixedThreadPool(poolsSize);
    }

    @Override
    synchronized public void listenImpl() throws ModbusIOException {
        try {
            server = new ServerSocket(tcp.getPort());
            mainThread = new Thread(this);
            setListening(true);
            mainThread.start();
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
    }

    @Override
    synchronized public void shutdownImpl() {
        try {
            if (server != null)
                server.close();
        } catch (IOException e) {
            Modbus.log().warning("Something wrong with server socket: " + e.getLocalizedMessage());
        } finally {
            server = null;
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
        try {
            if (mainThread != null) {
                mainThread.join(2000);
                if (mainThread.isAlive())
                    mainThread.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainThread = null;
        }
    }

    @Override
    public void run() {
        Socket s;
        ModbusConnection conn;
        try {
            while (isListening()) {
                s = server.accept();
                try {
                    conn = ModbusConnectionFactory.getTcpSlave(s);
                    conn.setReadTimeout(getReadTimeout());
                    threadPool.execute(new RequestHandlerTCP(this, conn));
                } catch (ModbusIOException ioe) {
                    Modbus.log().warning(ioe.getLocalizedMessage());
                    s.close();
                }
            }
        } catch (SocketException se) {
            if (server != null) {
                if (server.isClosed()) {
                    Modbus.log().fine("All right, server socket has been closed:" + se.getLocalizedMessage());
                } else {
                    Modbus.log().warning("Something wrong:" + se.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                shutdown();
            } catch (ModbusIOException e) {
                Modbus.log().warning("Cannot shutdown: " + e.getLocalizedMessage());
            }
        }
    }
}
