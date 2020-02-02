package com.gbw.scanner.plugins.scripts;

import com.gbw.scanner.Host;
import com.gbw.scanner.sink.SinkQueue;

public interface GBWScanScript {

    GBWScanScriptCommonConfig getConfig();

    boolean isAccept(Host host);

    void scan(Host host, SinkQueue sinkQueue);
    
}
