package com.gbw.scanner.source;

import java.util.List;

public class GBWFileLineSourceConfig  extends GBWHostSourceConfig{

    private String hostFile;
    private List<String> scanType;
    private String proto;
    private List<Integer> ports;

    public String getHostFile() {
        return hostFile;
    }

    public void setHostFile(String hostFile) {
        this.hostFile = hostFile;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }


    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    public List<String> getScanType() {
        return scanType;
    }

    public void setScanType(List<String> scanType) {
        this.scanType = scanType;
    }
}
