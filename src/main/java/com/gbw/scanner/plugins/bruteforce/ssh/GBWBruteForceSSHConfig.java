package com.gbw.scanner.plugins.bruteforce.ssh;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForceCommonConfig;

public class GBWBruteForceSSHConfig extends GBWBruteForceCommonConfig {

    private String proxyHost;
    private int proxyPort;
    private String proxyUser;
    private String proxyPasswd;

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPasswd() {
        return proxyPasswd;
    }

    public void setProxyPasswd(String proxyPasswd) {
        this.proxyPasswd = proxyPasswd;
    }

}
