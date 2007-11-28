/*
 * ***** BEGIN LICENSE BLOCK *****
 * 
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007 Zimbra, Inc.
 * 
 * The contents of this file are subject to the Yahoo! Public License
 * Version 1.0 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * 
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.cs.im;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.wildfire.XMPPServer;
import org.jivesoftware.wildfire.interceptor.InterceptorManager;

import com.zimbra.common.localconfig.LC;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.cs.account.Domain;
import com.zimbra.cs.account.Provisioning;
//import com.zimbra.cs.im.interop.Interop;
import com.zimbra.cs.im.provider.IMGlobalProperties;
import com.zimbra.cs.im.provider.IMLocalProperties;

public class ZimbraIM {
    
    private static boolean sRunning = false;
    
    public synchronized static void startup() throws ServiceException {
        try {
            
            JiveGlobals.setHomeDirectory(LC.zimbra_home.value());
            
            ArrayList<String> domainStrs = new ArrayList<String>();
            
            String defaultDomain = Provisioning.getInstance().getConfig().getAttr(Provisioning.A_zimbraDefaultDomainName, null);
            if (defaultDomain != null) {
                ZimbraLog.im.info("Setting default XMPP domain to: "+defaultDomain);
                domainStrs.add(defaultDomain);
            } 
            List<Domain> domains = Provisioning.getInstance().getAllDomains();
            for (Domain d : domains) {
                domainStrs.add(d.getName());
            }
            
            // set the special msgs ClassLoader -- so that WF looks in our conf/msgs directory
            // for its localization .properties bundles.
            org.jivesoftware.util.LocaleUtils.sMsgsClassLoader = com.zimbra.cs.util.L10nUtil.getMsgClassLoader();             
            
            XMPPServer srv = new XMPPServer(domainStrs, new IMLocalProperties(), new IMGlobalProperties());
            InterceptorManager.getInstance().addInterceptor(new com.zimbra.cs.im.PacketInterceptor());
            
            sRunning = true;
        } catch (Exception e) { 
            ZimbraLog.system.warn("Could not start XMPP server: " + e.toString());
            e.printStackTrace();
        }
    }
    
    public synchronized static void shutdown() {
        XMPPServer instance = XMPPServer.getInstance();
        if (instance != null)
            instance.stop();
        if (sRunning) {
            sRunning = false;
        }
    }

}
