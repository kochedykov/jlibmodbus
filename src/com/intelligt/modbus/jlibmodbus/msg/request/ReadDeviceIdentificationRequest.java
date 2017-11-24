package com.intelligt.modbus.jlibmodbus.msg.request;

import com.intelligt.modbus.jlibmodbus.msg.base.mei.MEIReadDeviceIdentification;
import com.intelligt.modbus.jlibmodbus.msg.base.mei.ReadDeviceIdentificationCode;
import com.intelligt.modbus.jlibmodbus.utils.MEITypeCode;

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
public class ReadDeviceIdentificationRequest extends EncapsulatedInterfaceTransportRequest {

    public ReadDeviceIdentificationRequest() {
        super();
        setMEIType(MEITypeCode.READ_DEVICE_IDENTIFICATION);
    }

    public void setObjectId(int objectId) {
        ((MEIReadDeviceIdentification) getMei()).setObjectId(objectId);
    }

    public void setReadDeviceId(ReadDeviceIdentificationCode readDeviceId) {
        ((MEIReadDeviceIdentification) getMei()).setReadDeviceId(readDeviceId);
    }
}
