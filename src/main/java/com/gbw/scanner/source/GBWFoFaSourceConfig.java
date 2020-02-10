package com.gbw.scanner.source;

import java.util.List;

public class GBWFoFaSourceConfig extends GBWHostSourceConfig {

    private String fofaFile;
    private List<String> scanType;
    private String proto;
    private int port;

    public String getFofaFile() {
        return fofaFile;
    }

    public void setFofaFile(String fofaFile) {
        this.fofaFile = fofaFile;
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
