README

The MODBUS protocol implementation in pure java.

The main advantages of the library are following:
- the library supports a lot of communication libraries (jssc, rxtx, purejavacomm, java comm api); 
- the library has (practically) complete implementation of the modbus protocol v1.1b.

AUTHORS

Vladislav Y. Kochedykov, software engineer, technical expert.

CONTACT

If you have problems, questions or suggestions, please contact me at email address vladislav.kochedykov@gmail.com

To learn how to use the library you can either use examples in "examples\com\invertor\examples\modbus" folder or contact me at email.

Maven dependency:

The latest stable release.
<dependency>
  <groupId>com.intelligt.modbus</groupId>
  <artifactId>jlibmodbus</artifactId>
  <version>1.2.9.7</version>
</dependency>

WEB SITE

project homepage:
http://jlibmodbus.sourceforge.net
https://github.com/kochedykov/jlibmodbus

purejavacomm homepage:
http://www.sparetimelabs.com/purejavacomm/purejavacomm.php
https://github.com/nyholku/purejavacomm

jssc home page:
https://code.google.com/p/java-simple-serial-connector
https://github.com/scream3r/java-simple-serial-connector

rxtx home page:
http://users.frii.com/jarvi/rxtx/index.html

jserialcomm homepage:
https://github.com/Fazecast/jSerialComm

-----------------------------------------------------------------------------------------------------------
----Release version 1.2.9.7 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* The automatic serial port connector selection is rolled back.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.9.6 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* IMPORTANTLY! Fixed the issue #22: MODBUS Master TCP - Socket left open.
	* Fixed a bug with listening for incoming data on Android Things device.
	* Add automatic selection of a serial port connector.
	* Add Android Things support.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.9.5 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* Fixed a bug with an incorrect conversion from two integers to a long value.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.9.4 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* Fixed incoming frame logging.
	* Fixed a bug in OutputStreamASCII - add unimplemented method write(byte[], int, int).
	* Fixed the issue #17: Incorrect logic if lrc equals zero. Thanks to https://github.com/mlasevich
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.9.3 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* Fixed hasNext() in ModbusValues iterator.
	* Fixed error in JSerialComm implementation. https://sourceforge.net/p/jlibmodbus/bugs/2/
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.9.2 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* Fixed a bug with setting timeout in SerialPortLoopback.
	* Fixed a bug with setting a slave timeout.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.9.1 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* Experimental! Implemented RTU over TCP. Add a new serial port factories TcpClient and TcpServer. Usage: //server side SerialUtils.setSerialPortFactory(new SerialPortFactoryTcpServer(tcpParameters)); ModbusSlave slave = ModbusSlaveFactory.createModbusSlaveRTU(serialParameters);
	* IMPORTANTLY! Fixed the issue #15: Bug in setRange method. Thanks to https://github.com/shdk
	* Added ModbusSlaveSerialObserver and SerialPortInfo classes to notify if the com port was opened or closed.
	* IMPORTANTLY! Added the jSerialComm library support.
	* IMPORTANTLY! Fixed the issue #14:Bug in DataHolder class. Thanks to https://github.com/shdk
	* Added API to retrieve information about a TCP client connected to the TCP slave.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.9.0 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* IMPORTANTLY! Renamed package from com.invertor.modbus to com.intelligt.modbus.jlibmodbus! 
	* IMPORTANTLY! Change default read timeout to 1000 ms.
	* Fixed a bug in GetCommEventLog.
	* Added data frame events to Input/Output streams.
	* Added the loopback serial port for testing applications without actual serial hardware. Author Kevin Kieffer.
	* Added method ReadCoilsResponse#getModbusCoils that returns a ModbusCoils instance.
	* Fixed a bug in ModbusValue#next().
	* Fixed errors in data conversion operations.
	* Added method ReadHoldingRegisters#getHoldingRegisters that returns a ModbusHoldingRegisters instance.
	* Renamed getBytes -> getBytesBe.
	* Renamed Coils->ModbusCoils, HoldingRegistors->ModbusHoldingRegisters.
	* Added class ModbusValues.
	* Renamed getValues -> getBytes, setValues -> setBytes.
	* Added additional helpers.
	* Added the BroadcastResponse class to avoid timeout exception while sending broadcast requests.
	* Fixed an incorrect logic if crc equals zero. Thanks to flooduk (https://github.com/flooduk).
	* Fixed handling of broadcast requests.
	* Fixed handling of RestartCommunicationsOption.
	* Optimized memory usage.
	* Fixed the MIN_PDU_LENGTH value.
	* Reduced copy operations number.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.8.4 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* Added basic maven pom file
	* Thread safety. Thanks to Kevin Kieffer.
	* HoldingRegisters and Coils is now observable. Thanks to Kevin Kieffer.
	* Added a method to get a byte-buffer from ReadHoldingRegistersResponse.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.8.3 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* IMPORTANTLY! Fixed bug #1 Support of SlaveID=255 with ModbusTCP. Thanks to Francois DELOYE (https://sourceforge.net/u/sesa258801).
	* Added broadcast support.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.8.2 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* Added a new method - SerialUtils#getConnectorVersion.
	* Fixed bug in ModbusMaster#disconnectImpl. Thanks to dpozimski (https://github.com/dpozimski)!
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.8.1 -------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* Fixed bug: setting of the "connected" status had no effect.
	* Added ModbuMaster#connectImpl and ModbusMaster#disconnectImpl methods.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.8 ---------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* Added a new method ModbusMaster#isConnected that returns true if a master is connected to a remote slave.
	* Added a new method ModbusSlave#isListening method that returns true if a slave is already listening for incoming connections.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.7 ---------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* IMPORTANTLY! Fixed bug with reading of multiple values in a request.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.6 ---------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* Fixed bug with a NPE thrown if setReadTimeout is invoked before port is opened.
	* IMPORTANTLY! Added the PureJavaComm library support.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.5 ---------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* Added readTimeout to ModbusSlave. 
	* API changes: Deleted a few factory-methods from ModbusMasterFactory and ModbusSlaveFactory.
	* API changes: Renamed ModbusEvent to ModbusCommEvent.
	* API changes: Added methods Coils#quantity() and HoldingRegisters#quantity().
	* API changes: ModbusSlaver#open() -> ModbusSlaver#listen(), ModbusSlaver#close() -> ModbusSlaver#shutdown().
	* API changes: ModbusMaster#open() -> ModbusMaster#connect(), ModbusMaster#close() -> ModbusMaster#disconnect().
	* Fixed bug that type of quantity must be double or float before it'll be divided.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.4 ---------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* Fixed bug with address and quantity checking in ReadCoilsRequest#checkAddressRange.
	* Improved search of a ModbusFunctionCode instance by value.
	* Fixed issues found by FindBug utility(http://findbugs.sourceforge.net).
	* Fixed bug with SerialPortJSSC#read(byte[] b, int off, int len) method.
	* Fixed bug with setting readTimeout before opening a connection.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.3 ---------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* Added a class to obtain the version of the library.
	* Improved logger output.
	* Added method getPortIdentifiers to get a list of available port identifiers.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.2 ---------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS
	* IMPORTANTLY! Added support for java communication api 3.0.
	* IMPORTANTLY! Migrated to the apache license version 2.0.
	* Methods "purgeRx", "purgeTx", "clear" are removed from SerialPort class.
	* Method "reset" is removed from ModbusConnection.
	* Fixed potential bug with infinite loop in ModbusMaster.readResponse.
	* Added method ModbusConnection.getReadTimeout.
	* Prevents a NullPointerException if master is used before it has been opened.
-----------------------------------------------------------------------------------------------------------
----Release version 1.2.1 ---------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS:
	* IMPORTANTLY! Add the RXTX library support! Usage: SerialUtils.setSerialPortFactory(new SerialPortFactoryRXTX());
	* Added checking if jssc library is available.
	* Fixed potential bug with reading of byte array.
	* Added avoiding some NPEs.
	* IMPORTANTLY! Fixed bug with flushing of output buffers.
	* Modbus RTU master is now able to connect via tcp socket (in case of rs232/rs485 to wifi adapters).
-----------------------------------------------------------------------------------------------------------
----Release version 1.2 -----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS:
	* Added extended logging.
	* Add auto-increment of transaction id. See Modbus.setAutoIncrementTransactionId.
-----------------------------------------------------------------------------------------------------------
----Release version 1.1 -----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* NEWS:
	* IMPORTANTLY! Fixed bug with offset reading.
	* IMPORTANTLY! Fixed data conversion issues.
-----------------------------------------------------------------------------------------------------------
----Release version 1.0 -----------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
	* Implemented MODBUS functions:
	* 0x01 - Read Coils
	* 0x02 - Read Discrete Inputs
	* 0x03 - Read Holding Registers
	* 0x04 - Read Input Registers
	* 0x05 - Write Single Coil
	* 0x06 - Write Single Register
	* 0x07 - Read Exception Status(serial line only)
	* 0x08 - Diagnostics(serial line only)
	* 0x0B - Get Comm Event Counter(serial line only)
	* 0x0C - Get Comm Event Log(serial line only)
	* 0x0F - Write Multiple Coils
	* 0x10 - Write Multiple Registers
	* 0x11 - Report Slave Id(serial line only)
	* 0x14 - Read File Record
	* 0x15 - Write File Record
	* 0x16 - Mask Write Register
	* 0x17 - Read Write Multiple Registers
	* 0x18 - Read Fifo Queue
	* 0x2B - Encapsulated Interface Tansport(Read Device Identification interface, (0x2B / 0x0E))
