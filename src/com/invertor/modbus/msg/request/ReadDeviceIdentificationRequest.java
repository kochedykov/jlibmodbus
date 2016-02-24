package com.invertor.modbus.msg.request;

import com.invertor.modbus.exception.ModbusNumberException;
import com.invertor.modbus.msg.base.mei.MEIReadDeviceIdentification;
import com.invertor.modbus.msg.base.mei.ReadDeviceIdentificationCode;
import com.invertor.modbus.utils.MEITypeCode;

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
public class ReadDeviceIdentificationRequest extends EncapsulatedInterfaceTransportRequest {

    public ReadDeviceIdentificationRequest(int serverAddress) throws ModbusNumberException {
        super(serverAddress);
        setMEIType(MEITypeCode.READ_DEVICE_IDENTIFICATION);
    }

    public ReadDeviceIdentificationRequest(int serverAddress, int objectId, ReadDeviceIdentificationCode readDeviceId) throws ModbusNumberException {
        super(serverAddress);
        setMEIType(MEITypeCode.READ_DEVICE_IDENTIFICATION);
        MEIReadDeviceIdentification mei = (MEIReadDeviceIdentification) getMei();
        mei.setObjectId(objectId);
        mei.setReadDeviceId(readDeviceId);
    }

    public void setObjectId(int objectId) {
        ((MEIReadDeviceIdentification) getMei()).setObjectId(objectId);
    }

    public void setReadDeviceId(ReadDeviceIdentificationCode readDeviceId) {
        ((MEIReadDeviceIdentification) getMei()).setReadDeviceId(readDeviceId);
    }
}
