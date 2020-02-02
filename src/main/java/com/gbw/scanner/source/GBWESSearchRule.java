package com.gbw.scanner.source;

import java.util.List;

public class GBWESSearchRule {

    private String[] indices;
    private String type;

    private String ipField;
    private String hostField;
    private String portField;
    private String timeField;

    private String search;
    private List<String> scanType;
    private String proto;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIpField() {
        return ipField;
    }

    public void setIpField(String ipField) {
        this.ipField = ipField;
    }

    public String getHostField() {
        return hostField;
    }

    public void setHostField(String hostField) {
        this.hostField = hostField;
    }

    public String getPortField() {
        return portField;
    }

    public void setPortField(String portField) {
        this.portField = portField;
    }

    public String getTimeField() {
        return timeField;
    }

    public void setTimeField(String timeField) {
        this.timeField = timeField;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }



    public String[] getIndices() {
        return indices;
    }

    public void setIndices(String[] indices) {
        this.indices = indices;
    }



    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public List<String> getScanType() {
        return scanType;
    }

    public void setScanType(List<String> scanType) {
        this.scanType = scanType;
    }
}
