package com.gbw.scanner.source.iprange;

import com.gbw.scanner.source.GBWHostSourceConfig;

import java.util.List;

public class GBWIPRangeSourceConfig  extends GBWHostSourceConfig {

    private String wlistPath;
    private String blistPath;

    private List<GBWIPRangePort2ScanType> port2ScanTypes;


    public String getWlistPath() {
        return wlistPath;
    }

    public void setWlistPath(String wlistPath) {
        this.wlistPath = wlistPath;
    }

    public String getBlistPath() {
        return blistPath;
    }

    public void setBlistPath(String blistPath) {
        this.blistPath = blistPath;
    }

    public List<GBWIPRangePort2ScanType> getPort2ScanTypes() {
        return port2ScanTypes;
    }

    public void setPort2ScanTypes(List<GBWIPRangePort2ScanType> port2ScanTypes) {
        this.port2ScanTypes = port2ScanTypes;
    }
}
