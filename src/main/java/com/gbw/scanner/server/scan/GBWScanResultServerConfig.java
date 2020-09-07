package com.gbw.scanner.server.scan;

import com.gbw.scanner.geoip.GeoIPConfigItem;
import com.gbw.scanner.sink.es.ESConfigItem;
import com.gbw.scanner.sink.file.FileSinkConfig;

public class GBWScanResultServerConfig {

    private boolean isSSL;
    private String ip;
    private int port;

    private String sinkType;
    private ESConfigItem esSinkConfig;
    private FileSinkConfig fileSinkConfig;
    private GeoIPConfigItem geoIPConfig;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSSL() {
        return isSSL;
    }

    public void setSSL(boolean SSL) {
        isSSL = SSL;
    }


    public String getSinkType() {
        return sinkType;
    }

    public void setSinkType(String sinkType) {
        this.sinkType = sinkType;
    }

    public ESConfigItem getEsSinkConfig() {
        return esSinkConfig;
    }

    public void setEsSinkConfig(ESConfigItem esSinkConfig) {
        this.esSinkConfig = esSinkConfig;
    }

    public FileSinkConfig getFileSinkConfig() {
        return fileSinkConfig;
    }

    public void setFileSinkConfig(FileSinkConfig fileSinkConfig) {
        this.fileSinkConfig = fileSinkConfig;
    }

    public GeoIPConfigItem getGeoIPConfig() {
        return geoIPConfig;
    }

    public void setGeoIPConfig(GeoIPConfigItem geoIPConfig) {
        this.geoIPConfig = geoIPConfig;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
