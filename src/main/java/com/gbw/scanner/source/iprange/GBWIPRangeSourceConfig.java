package com.gbw.scanner.source.iprange;

import com.gbw.scanner.source.GBWHostSourceConfig;

import java.util.List;

public class GBWIPRangeSourceConfig  extends GBWHostSourceConfig {

    private String wlistPath;
    private String blistPath;
    private String statusFName;
    private String assetsFile;

    private long tv;
    private boolean isAssetsFile;

    private List<GBWIPRangePort2ScanType> port2ScanTypes;

    public String getAssetsFile() {
        return assetsFile;
    }

    public void setAssetsFile(String assetsFile) {
        this.assetsFile = assetsFile;
    }

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

    public String getStatusFName() {
        return statusFName;
    }

    public void setStatusFName(String statusFName) {
        this.statusFName = statusFName;
    }

    public long getTv() {
        return tv;
    }

    public void setTv(long tv) {
        this.tv = tv;
    }

    public boolean isAssetsFile() {
        return isAssetsFile;
    }

    public void setAssetsFile(boolean assetsFile) {
        isAssetsFile = assetsFile;
    }
}
