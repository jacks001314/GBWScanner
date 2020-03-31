package com.gbw.scanner.plugins.bruteforce.redis;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForceCommonConfig;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class GBWBruteForceRedisConfig extends GBWBruteForceCommonConfig {

    private  boolean isSSL;

    public boolean isSSL() {
        return isSSL;
    }

    public void setSSL(boolean SSL) {
        isSSL = SSL;
    }

    public void addOpts(Options opts){

        super.addOpts(opts);
        opts.addOption("ssl",false,"use ssl or not");

    }

    public void initFromOpts(CommandLine cmdLine) throws IllegalArgumentException{
        super.initFromOpts(cmdLine);

        isSSL = false;

        if(cmdLine.hasOption("ssl"))
            isSSL = true;
    }
}
