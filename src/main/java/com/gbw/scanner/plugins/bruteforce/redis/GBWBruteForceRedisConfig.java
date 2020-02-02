package com.gbw.scanner.plugins.bruteforce.redis;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForceCommonConfig;

public class GBWBruteForceRedisConfig extends GBWBruteForceCommonConfig {

    private  boolean isSSL;

    public boolean isSSL() {
        return isSSL;
    }

    public void setSSL(boolean SSL) {
        isSSL = SSL;
    }
}
