/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 Zimbra, Inc.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.unittest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.*;
import static org.junit.Assert.*;

import com.zimbra.cs.account.AccountServiceException;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.account.Provisioning.ServerBy;
import com.zimbra.cs.account.Server;

public class TestLdapProvServer {
    private static Provisioning prov;
    
    @BeforeClass
    public static void init() throws Exception {
        TestLdap.manualInit();
        
        prov = Provisioning.getInstance();
    }
    
    private Server createServer(String serverName) throws Exception {
        return createServer(serverName, null);
    }
    
    private Server createServer(String serverName, Map<String, Object> attrs) throws Exception {
        if (attrs == null) {
            attrs = new HashMap<String, Object>();
        }
        Server server = prov.get(ServerBy.name, serverName);
        assertNull(server);
        server = prov.createServer(serverName, attrs);
        assertNotNull(server);
        return server;
    }
    
    @Test
    public void createServer() throws Exception {
        String SERVER_NAME = "createServer";
        Server server = createServer(SERVER_NAME);
        prov.deleteServer(server.getId());
    }
    
    @Test
    public void createServerAlreadyExists() throws Exception {
        String SERVER_NAME = "createServerAlreadyExists";
        Server server = createServer(SERVER_NAME);
        
        boolean caughtException = false;
        try {
            prov.createServer(SERVER_NAME, new HashMap<String, Object>());
        } catch (AccountServiceException e) {
            if (AccountServiceException.SERVER_EXISTS.equals(e.getCode())) {
                caughtException = true;
            }
        }
        assertTrue(caughtException);
        
        prov.deleteServer(server.getId());
    }
    
    @Test
    public void localServer() throws Exception {
        Server localServer = prov.getLocalServer();
        assertNotNull(localServer);
    }
    
    @Test
    public void getAllServers() throws Exception {
        String SERVER_NAME_1 = "getAllServers-1";
        Map<String, Object> server1Attrs = new HashMap<String, Object>();
        server1Attrs.put(Provisioning.A_zimbraServiceEnabled, 
                new String[]{Provisioning.SERVICE_MEMCACHED, Provisioning.SERVICE_MAILBOX});
        Server server1 = createServer(SERVER_NAME_1, server1Attrs);
        
        String SERVER_NAME_2 = "getAllServers-2";
        Server server2 = createServer(SERVER_NAME_2);
        
        List<Server> allServers = prov.getAllServers();
        assertEquals(3, allServers.size());
        
        Set<String> allServerIds = new HashSet<String>();
        for (Server server : allServers) {
            allServerIds.add(server.getId());
        }
        assertTrue(allServerIds.contains(prov.getLocalServer().getId()));
        assertTrue(allServerIds.contains(server1.getId()));
        assertTrue(allServerIds.contains(server2.getId()));
        
        List<Server> allServersByService = prov.getAllServers(Provisioning.SERVICE_MEMCACHED);
        assertEquals(1, allServersByService.size());
        assertEquals(server1.getId(), allServersByService.get(0).getId());
        
        prov.deleteServer(server1.getId());
        prov.deleteServer(server2.getId());
    }

}
