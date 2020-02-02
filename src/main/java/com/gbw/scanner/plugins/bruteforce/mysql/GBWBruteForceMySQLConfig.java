package com.gbw.scanner.plugins.bruteforce.mysql;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForceCommonConfig;

public class GBWBruteForceMySQLConfig extends GBWBruteForceCommonConfig {

    private boolean isSSL;

    public boolean isSSL() {
        return isSSL;
    }

    public void setSSL(boolean SSL) {
        isSSL = SSL;
    }
}
