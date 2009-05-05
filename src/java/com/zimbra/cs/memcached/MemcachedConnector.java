/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009 Zimbra, Inc.
 * 
 * The contents of this file are subject to the Yahoo! Public License
 * Version 1.0 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */

package com.zimbra.cs.memcached;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.spy.memcached.HashAlgorithm;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.ZimbraMemcachedClient;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.account.Server;

public class MemcachedConnector {

    private static ZimbraMemcachedClient sTheClient = new ZimbraMemcachedClient();

    /**
     * Returns the one and only memcached client object.
     * @return
     */
    public static ZimbraMemcachedClient getClient() {
        return sTheClient;
    }

    /**
     * Startup the memcached connection.  Establish the memcached connection(s) if configured.
     * @throws ServiceException
     */
    public static void startup() throws ServiceException {
        reloadConfig();
    }

    /**
     * Are we currently connected to the memcached servers?
     * @return
     */
    public static boolean isConnected() {
        return sTheClient.isConnected();
    }

    /**
     * Reload the memcached client configuration.  Connect to the servers if configured with a
     * non-empty server list.  Any old connections are flushed and disconnected.
     * @throws ServiceException
     */
    public static void reloadConfig() throws ServiceException {
        Server server = Provisioning.getInstance().getLocalServer();
        String[] serverList = server.getMultiAttr(Provisioning.A_zimbraMemcachedClientServerList);
        boolean useBinaryProtocol = server.getBooleanAttr(Provisioning.A_zimbraMemcachedClientBinaryProtocolEnabled, false);
        String hashAlgorithm = server.getAttr(Provisioning.A_zimbraMemcachedClientHashAlgorithm, HashAlgorithm.KETAMA_HASH.toString());
        int expirySeconds = (int) server.getLongAttr(Provisioning.A_zimbraMemcachedClientExpirySeconds, 86400);
        long timeoutMillis = server.getLongAttr(Provisioning.A_zimbraMemcachedClientTimeoutMillis, 1000);
        List<InetSocketAddress> serverAddrs = getServerAddresses(serverList);
        sTheClient.connect(serverAddrs, useBinaryProtocol, hashAlgorithm, expirySeconds, timeoutMillis);
    }

    /**
     * Shutdown the memcached connection.
     * @throws ServiceException
     */
    public static void shutdown() throws ServiceException {
        sTheClient.disconnect(30000);
    }

    private static List<InetSocketAddress> getServerAddresses(String[] servers) {
        List<InetSocketAddress> serverAddrs;
        if (servers != null && servers.length > 0)
            serverAddrs = ZimbraMemcachedClient.parseServerList(servers);
        else
            serverAddrs = new ArrayList<InetSocketAddress>(0);
        return serverAddrs;
    }
}
