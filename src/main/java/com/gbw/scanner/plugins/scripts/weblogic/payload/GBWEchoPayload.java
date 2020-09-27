package com.gbw.scanner.plugins.scripts.weblogic.payload;

public interface GBWEchoPayload<T> {

    T getObject(String version,Class echoClass) throws Exception;

    String getName();
}
