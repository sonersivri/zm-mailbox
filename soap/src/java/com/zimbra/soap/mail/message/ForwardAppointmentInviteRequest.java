/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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

package com.zimbra.soap.mail.message;

import com.google.common.base.MoreObjects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zimbra.common.soap.MailConstants;
import com.zimbra.soap.mail.type.Msg;

/**
 * @zm-api-command-auth-required true
 * @zm-api-command-admin-auth-required false
 * @zm-api-command-description Used by an attendee to forward an appointment invite email to another user who is
 * not already an attendee.
 * <br />
 * To forward an appointment item, use ForwardAppointmentRequest instead.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=MailConstants.E_FORWARD_APPOINTMENT_INVITE_REQUEST)
public class ForwardAppointmentInviteRequest {

    /**
     * @zm-api-field-tag invite-message-item-id
     * @zm-api-field-description Invite message item ID
     */
    @XmlAttribute(name=MailConstants.A_ID /* id */, required=false)
    private String id;

    // E_INVITE child is not allowed
    /**
     * @zm-api-field-description Details of the invite
     */
    @XmlElement(name=MailConstants.E_MSG /* m */, required=false)
    private Msg msg;

    public ForwardAppointmentInviteRequest() {
    }

    public void setId(String id) { this.id = id; }
    public void setMsg(Msg msg) { this.msg = msg; }
    public String getId() { return id; }
    public Msg getMsg() { return msg; }

    public MoreObjects.ToStringHelper addToStringInfo(MoreObjects.ToStringHelper helper) {
        return helper
            .add("id", id)
            .add("msg", msg);
    }

    @Override
    public String toString() {
        return addToStringInfo(MoreObjects.toStringHelper(this)).toString();
    }
}
