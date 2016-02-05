package com.sbpinvertor.modbus.slave;

import com.sbpinvertor.modbus.Modbus;
import com.sbpinvertor.modbus.ModbusSlave;
import com.sbpinvertor.modbus.exception.ModbusIOException;
import com.sbpinvertor.modbus.net.ModbusConnection;
import com.sbpinvertor.modbus.net.ModbusSlaveConnectionTCP;
import com.sbpinvertor.modbus.tcp.TcpParameters;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class ModbusSlaveTCP extends ModbusSlave implements Runnable {

    final static public int DEFAULT_POOLS_SIZE = 10;
    final private ExecutorService threadPool;
    final private TcpParameters tcp;
    private Thread mainThread = null;
    private ServerSocket server = null;
    private volatile boolean listening = false;

    public ModbusSlaveTCP(TcpParameters tcp) {
        this(tcp, DEFAULT_POOLS_SIZE);
    }

    public ModbusSlaveTCP(TcpParameters tcp, int poolsSize) {
        this.tcp = new TcpParameters(tcp);
        threadPool = Executors.newFixedThreadPool(poolsSize);
    }

    @Override
    public void open() throws ModbusIOException {
        try {
            server = new ServerSocket(tcp.getPort());
            listening = true;
            mainThread = new Thread(this);
            mainThread.start();
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }
    }

    @Override
    public void close() {
        listening = false;

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
        Socket client;
        ModbusConnection conn;
        try {
            while (listening) {
                client = server.accept();
                conn = new ModbusSlaveConnectionTCP(client);
                conn.setReadTimeout(10000);
                threadPool.execute(new RequestHandlerTCP(this, conn));
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
            close();
        }
    }
}
