/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2021 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.soap.sync.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.MoreObjects;
import com.zimbra.common.soap.SyncConstants;
import com.zimbra.soap.sync.type.DeviceId;

/**
 * @zm-api-command-network-edition
 * @zm-api-command-auth-required true
 * @zm-api-command-admin-auth-required false
 * @zm-api-command-description block sync with a device.
 * This will cause a policy reset, but will not reset sync data.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=SyncConstants.E_BLOCK_DEVICE_REQUEST)
public class BlockDeviceRequest {

    /**
     * @zm-api-field-description Specify the device to resume
     */
    @XmlElement(name=SyncConstants.E_DEVICE /* device */, required=true)
    private final DeviceId device;

    /**
     * no-argument constructor wanted by JAXB
     */
    @SuppressWarnings("unused")
    private BlockDeviceRequest() {
        this((DeviceId) null);
    }

    public BlockDeviceRequest(DeviceId device) {
        this.device = device;
    }

    public DeviceId getDevice() { return device; }

    public MoreObjects.ToStringHelper addToStringInfo(MoreObjects.ToStringHelper helper) {
        return helper.add("device", device);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this)).toString();
    }
}
