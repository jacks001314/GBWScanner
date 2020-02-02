package com.gbw.scanner.plugins.bruteforce;

import com.gbw.scanner.Host;

import java.io.IOException;

public abstract class GBWAbstractBruteForce implements GBWBruteForce {

    protected GBWDict dict;

    public GBWAbstractBruteForce(GBWBruteForceCommonConfig config) throws IOException, InterruptedException {

        dict = new GBWDict(config.getUserFName(),config.getPasswdFName());
    }

    @Override
    public String getProto(Host host) {

        return host.getProto();
    }
    @Override
    public GBWDict getDict() {
        return dict;
    }

}
