package com.gbw.scanner.source;

import com.gbw.scanner.Host;
import com.xmap.api.SourceException;

public interface GBWHostSource {

    boolean isEmpty();

    Host get();

    void put(Host host);

    void start() throws SourceException;
    void stop();
}







