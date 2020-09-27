package com.gbw.scanner.plugins.scripts.weblogic.payload;

import weblogic.corba.utils.MarshalledObject;

public class GBWMarshalledObjectEcho implements GBWEchoPayload<Object> {

    private GBWEchoPayload payload;

    public GBWMarshalledObjectEcho(GBWEchoPayload payload) {
        this.payload = payload;
    }

    public Object getObject(String version,Class echoClass) throws Exception {
        return new MarshalledObject(payload.getObject(version,echoClass));
    }

    @Override
    public String getName() {
        return "GBWMarshalledObjectEcho";
    }

}
