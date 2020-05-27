package com.gbw.scanner;

import com.gbw.scanner.cmd.GBWCmdConfig;
import com.gbw.scanner.geoip.GeoIPConfigItem;
import com.gbw.scanner.plugins.bruteforce.GBWBruteForceConfig;
import com.gbw.scanner.plugins.detect.GBWDetectConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptConfigItem;
import com.gbw.scanner.plugins.webscan.GBWWebScanConfig;
import com.gbw.scanner.sink.es.ESConfigItem;
import com.gbw.scanner.sink.file.FileSinkConfig;
import com.gbw.scanner.source.GBWHostSourcePoolConfig;
import com.gbw.scanner.source.elasticsearch.GBWESSourceConfig;
import com.gbw.scanner.source.file.GBWFileLineSourceConfig;
import com.gbw.scanner.source.fofa.GBWFoFaSourceConfig;
import com.gbw.scanner.source.shodan.GBWShodanSourceConfig;

public class GBWScannerConfig {


    public static final String esSink = "esSink";
    public static final String fileSink = "fileSink";

    private String stype;

    private GBWHostSourcePoolConfig hostSourcePoolConfig;

    private boolean isOnBruteForce;
    private boolean isOnDetect;
    private boolean isOnWebDetect;
    private boolean isOnScanScript;

    private GBWBruteForceConfig bruteForceConfig;
    private GBWDetectConfig detectConfig;
    private GBWWebScanConfig webScanConfig;
    private GBWScanScriptConfigItem scanScriptConfig;

    private String sinkType;
    private ESConfigItem esSinkConfig;
    private FileSinkConfig fileSinkConfig;
    private GeoIPConfigItem geoIPConfig;

    private GBWCmdConfig cmdConfig;

    public String getSinkType() {
        return sinkType;
    }

    public void setSinkType(String sinkType) {
        this.sinkType = sinkType;
    }

    public FileSinkConfig getFileSinkConfig() {
        return fileSinkConfig;
    }

    public void setFileSinkConfig(FileSinkConfig fileSinkConfig) {
        this.fileSinkConfig = fileSinkConfig;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public boolean isOnBruteForce() {
        return isOnBruteForce;
    }

    public void setOnBruteForce(boolean onBruteForce) {

        this.isOnBruteForce = onBruteForce;
    }



    public boolean isOnWebDetect() {
        return isOnWebDetect;
    }

    public void setOnWebDetect(boolean onWebDetect) {
        isOnWebDetect = onWebDetect;
    }

    public boolean isOnDetect() {
        return isOnDetect;
    }

    public void setOnDetect(boolean onDetect) {
        isOnDetect = onDetect;
    }

    public GBWBruteForceConfig getBruteForceConfig() {
        return bruteForceConfig;
    }

    public void setBruteForceConfig(GBWBruteForceConfig bruteForceConfig) {
        this.bruteForceConfig = bruteForceConfig;
    }

    public GBWDetectConfig getDetectConfig() {
        return detectConfig;
    }

    public void setDetectConfig(GBWDetectConfig detectConfig) {
        this.detectConfig = detectConfig;
    }

    public GBWWebScanConfig getWebScanConfig() {
        return webScanConfig;
    }

    public void setWebScanConfig(GBWWebScanConfig webScanConfig) {
        this.webScanConfig = webScanConfig;
    }

    public GeoIPConfigItem getGeoIPConfig() {
        return geoIPConfig;
    }

    public void setGeoIPConfig(GeoIPConfigItem geoIPConfig) {
        this.geoIPConfig = geoIPConfig;
    }

    public ESConfigItem getEsSinkConfig() {
        return esSinkConfig;
    }

    public void setEsSinkConfig(ESConfigItem esSinkConfig) {
        this.esSinkConfig = esSinkConfig;
    }

    public GBWScanScriptConfigItem getScanScriptConfig() {
        return scanScriptConfig;
    }

    public void setScanScriptConfig(GBWScanScriptConfigItem scanScriptConfig) {
        this.scanScriptConfig = scanScriptConfig;
    }

    public boolean isOnScanScript() {
        return isOnScanScript;
    }

    public void setOnScanScript(boolean onScanScript) {
        isOnScanScript = onScanScript;
    }

    public GBWHostSourcePoolConfig getHostSourcePoolConfig() {
        return hostSourcePoolConfig;
    }

    public void setHostSourcePoolConfig(GBWHostSourcePoolConfig hostSourcePoolConfig) {
        this.hostSourcePoolConfig = hostSourcePoolConfig;
    }

    public GBWCmdConfig getCmdConfig() {
        return cmdConfig;
    }

    public void setCmdConfig(GBWCmdConfig cmdConfig) {
        this.cmdConfig = cmdConfig;
    }
}
