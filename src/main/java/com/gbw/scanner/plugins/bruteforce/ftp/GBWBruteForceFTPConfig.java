package com.gbw.scanner.plugins.bruteforce.ftp;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForceCommonConfig;

public class GBWBruteForceFTPConfig extends GBWBruteForceCommonConfig {

    private boolean isImplicit;
    private String trustmgr;
    private String serverType;
    private String proxyHost;
    private int proxyPort;
    private String proxyUser;
    private String proxyPasswd;

    public GBWBruteForceFTPConfig(){

        this.isImplicit = false;
        this.trustmgr = "";
        this.serverType = "";
    }

    public boolean isImplicit() {
        return isImplicit;
    }

    public void setImplicit(boolean implicit) {
        isImplicit = implicit;
    }

    public String getTrustmgr() {
        return trustmgr;
    }

    public void setTrustmgr(String trustmgr) {
        this.trustmgr = trustmgr;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

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
