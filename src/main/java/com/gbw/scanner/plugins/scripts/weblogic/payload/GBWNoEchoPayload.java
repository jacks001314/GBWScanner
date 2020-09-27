package com.gbw.scanner.plugins.scripts.weblogic.payload;


import com.gbw.scanner.Host;

import java.io.IOException;

public interface GBWNoEchoPayload<T> {

    T getObject(Host host) throws IOException;

    String getName();
}
