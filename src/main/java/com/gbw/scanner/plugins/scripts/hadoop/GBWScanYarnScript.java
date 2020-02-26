package com.gbw.scanner.plugins.scripts.hadoop;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.sink.SinkQueue;

public class GBWScanYarnScript implements GBWScanScript {

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return null;
    }

    @Override
    public boolean isAccept(Host host) {
        return false;
    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

    }
    
}
