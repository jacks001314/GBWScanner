package com.gbw.scanner.plugins.scripts.web.citrix;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.sink.SinkQueue;

public class GBWScanCitrixCVA_2019_19781Script implements GBWScanScript {

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
