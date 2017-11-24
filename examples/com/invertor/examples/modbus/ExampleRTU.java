package com.invertor.examples.modbus;

import com.invertor.modbus.*;
import com.invertor.modbus.data.ModbusHoldingRegisters;
import com.invertor.modbus.exception.ModbusIOException;
import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.exception.ModbusProtocolException;
import com.invertor.modbus.msg.request.ReadHoldingRegistersRequest;
import com.invertor.modbus.msg.response.ReadHoldingRegistersResponse;
import com.invertor.modbus.serial.SerialParameters;
import com.invertor.modbus.serial.SerialPortException;
import com.invertor.modbus.serial.SerialPortFactoryLoopback;
import com.invertor.modbus.serial.SerialUtils;

import java.net.UnknownHostException;

public class ExampleRTU {

    final static private int slaveId = 1;

    public static void main(String[] argv) {
        try {
            SerialParameters serialParameters = new SerialParameters();
            /*
             Use a virtual serial port SerialPortLoopback
             */
            SerialUtils.setSerialPortFactory(new SerialPortFactoryLoopback(false));
            ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveRTU(serialParameters);
            
            SerialUtils.setSerialPortFactory(new SerialPortFactoryLoopback(true));
            ModbusMaster master = ModbusMasterFactory.createModbusMasterRTU(serialParameters);

            master.setResponseTimeout(1000);
            slave.setServerAddress(slaveId);
            slave.setBroadcastEnabled(true);
            slave.setReadTimeout(10000);

            ModbusHoldingRegisters holdingRegisters = new ModbusHoldingRegisters(1000);

            for (int i = 0; i < holdingRegisters.getQuantity(); i++) {
                //fill
                holdingRegisters.set(i, i + 1);
            }

            //place the number PI at offset 0
            holdingRegisters.setFloat64At(0, Math.PI);

            slave.getDataHolder().setHoldingRegisters(holdingRegisters);

            slave.listen();

            master.connect();

            //prepare request
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest();
            request.setServerAddress(1);
            request.setStartAddress(0);
            request.setQuantity(10);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) request.getResponse();

            master.processRequest(request);
            ModbusHoldingRegisters registers = response.getHoldingRegisters();
            for (int r : registers) {
                System.out.println(r);
            }
            //get float
            System.out.println("PI is approximately equal to " + registers.getFloat64At(0));
            System.out.println();

            master.disconnect();
            slave.shutdown();
        } catch (ModbusProtocolException e) {
            e.printStackTrace();
        } catch (ModbusIOException e) {
            e.printStackTrace();
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
