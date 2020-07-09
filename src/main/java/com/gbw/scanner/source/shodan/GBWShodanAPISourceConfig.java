package com.gbw.scanner.source.shodan;

import com.gbw.scanner.source.GBWHostSourceConfig;

import java.util.List;

public class GBWShodanAPISourceConfig extends GBWHostSourceConfig {

    private String key;
    private boolean isBase64;
    private String search;
    private List<String> scanType;
    private String proto;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean isBase64() {
        return isBase64;
    }

    public void setBase64(boolean base64) {
        isBase64 = base64;
    }

    public List<String> getScanType() {
        return scanType;
    }

    public void setScanType(List<String> scanType) {
        this.scanType = scanType;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }
}
