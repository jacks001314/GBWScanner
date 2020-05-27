package com.gbw.scanner.source;

import com.gbw.scanner.Host;

public  interface GBWHostSourcePool {

    boolean isFull();

    boolean isEmpty();

    Host get();

    void put(Host host);

    void start();

    void stop();

    void addSource(GBWHostSource source);
    void removeSource(GBWHostSource source);
    GBWHostSource getHostSource(GBWHostSource curSource);
}
