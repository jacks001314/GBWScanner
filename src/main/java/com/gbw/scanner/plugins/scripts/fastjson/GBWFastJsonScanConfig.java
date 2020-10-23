package com.gbw.scanner.plugins.scripts.fastjson;

import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;

import java.util.List;

public class GBWFastJsonScanConfig extends GBWScanScriptCommonConfig {

    private String tplDir;
    private String dnslogDomain;
    private String dnslogHost;
    private int dnslogPort;
    private String proto;
    private String uri;

    private String urlPath;

    private int scanSleepTime;

    private List<GBWFastJsonTplEntry> entries;

    public String getTplDir() {
        return tplDir;
    }

    public void setTplDir(String tplDir) {
        this.tplDir = tplDir;
    }

    public String getDnslogDomain() {
        return dnslogDomain;
    }

    public void setDnslogDomain(String dnslogDomain) {
        this.dnslogDomain = dnslogDomain;
    }

    public String getDnslogHost() {
        return dnslogHost;
    }

    public void setDnslogHost(String dnslogHost) {
        this.dnslogHost = dnslogHost;
    }

    public int getDnslogPort() {
        return dnslogPort;
    }

    public void setDnslogPort(int dnslogPort) {
        this.dnslogPort = dnslogPort;
    }

    public List<GBWFastJsonTplEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<GBWFastJsonTplEntry> entries) {
        this.entries = entries;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }


    public int getScanSleepTime() {
        return scanSleepTime;
    }

    public void setScanSleepTime(int scanSleepTime) {
        this.scanSleepTime = scanSleepTime;
    }
}
