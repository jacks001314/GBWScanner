package com.gbw.scanner.source;

import java.util.List;

public class GBWShodanSourceConfig extends GBWHostSourceConfig {

    private String shodanFile;
    private List<String> scanType;
    private String proto;

    public GBWShodanSourceConfig() {
    }

    public String getShodanFile() {
        return this.shodanFile;
    }

    public void setShodanFile(String shodanFile) {
        this.shodanFile = shodanFile;
    }

    public List<String> getScanType() {
        return this.scanType;
    }

    public void setScanType(List<String> scanType) {
        this.scanType = scanType;
    }

    public String getProto() {
        return this.proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }
}
