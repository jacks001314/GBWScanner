package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;

public class GBWScanWeblogicConfig extends GBWScanScriptCommonConfig {

    private String payloadScriptDir;
    private String cmd;
    private String[] versions;
    private String defaultVersion;
    private String echoShell;

    private long scanSleepTime;
    private String httpLogIP;
    private int httpLogPort;
    private String coherenceIP;
    private int coherencePort;

    private String httpClientCmd;

    private boolean isonCVE_2020_2555;
    private boolean isonCVE_2020_2551;
    private boolean isonCVE_2019_2729;
    private boolean isOnCVE_2019_2725;
    private boolean isOnCVE_2018_3245;
    private boolean isOnCVE_2018_3191;
    private boolean isOnCVE_2018_2894;
    private boolean isOnCVE_2018_2893;
    private boolean isOnCVE_2018_2628;
    private boolean isOnCVE_2017_10271;
    private boolean isOnCVE_2017_3248;
    private boolean isOnCVE_2016_3510;
    private boolean isOnCVE_2016_0638;

    public String getHttpClientCmd() {
        return httpClientCmd;
    }

    public void setHttpClientCmd(String httpClientCmd) {
        this.httpClientCmd = httpClientCmd;
    }

    public String getDefaultVersion() {
        return defaultVersion;
    }

    public String getPayloadScriptDir() {
        return payloadScriptDir;
    }



    public String[] getVersions() {
        return versions;
    }



    public void setDefaultVersion(String defaultVersion) {
        this.defaultVersion = defaultVersion;
    }

    public void setPayloadScriptDir(String payloadScriptDir) {
        this.payloadScriptDir = payloadScriptDir;
    }

    public void setVersions(String[] versions) {
        this.versions = versions;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getEchoShell() {
        return echoShell;
    }

    public void setEchoShell(String echoShell) {
        this.echoShell = echoShell;
    }

    public long getScanSleepTime() {
        return scanSleepTime;
    }

    public void setScanSleepTime(long scanSleepTime) {
        this.scanSleepTime = scanSleepTime;
    }

    public String getHttpLogIP() {
        return httpLogIP;
    }

    public void setHttpLogIP(String httpLogIP) {
        this.httpLogIP = httpLogIP;
    }

    public int getHttpLogPort() {
        return httpLogPort;
    }

    public void setHttpLogPort(int httpLogPort) {
        this.httpLogPort = httpLogPort;
    }

    public String getCoherenceIP() {
        return coherenceIP;
    }

    public void setCoherenceIP(String coherenceIP) {
        this.coherenceIP = coherenceIP;
    }

    public int getCoherencePort() {
        return coherencePort;
    }

    public void setCoherencePort(int coherencePort) {
        this.coherencePort = coherencePort;
    }


    public boolean isOnCVE_2019_2725() {
        return isOnCVE_2019_2725;
    }

    public void setOnCVE_2019_2725(boolean onCVE_2019_2725) {
        isOnCVE_2019_2725 = onCVE_2019_2725;
    }

    public boolean isOnCVE_2018_3245() {
        return isOnCVE_2018_3245;
    }

    public void setOnCVE_2018_3245(boolean onCVE_2018_3245) {
        isOnCVE_2018_3245 = onCVE_2018_3245;
    }

    public boolean isOnCVE_2018_3191() {
        return isOnCVE_2018_3191;
    }

    public void setOnCVE_2018_3191(boolean onCVE_2018_3191) {
        isOnCVE_2018_3191 = onCVE_2018_3191;
    }

    public boolean isOnCVE_2018_2894() {
        return isOnCVE_2018_2894;
    }

    public void setOnCVE_2018_2894(boolean onCVE_2018_2894) {
        isOnCVE_2018_2894 = onCVE_2018_2894;
    }

    public boolean isOnCVE_2018_2893() {
        return isOnCVE_2018_2893;
    }

    public void setOnCVE_2018_2893(boolean onCVE_2018_2893) {
        isOnCVE_2018_2893 = onCVE_2018_2893;
    }

    public boolean isOnCVE_2018_2628() {
        return isOnCVE_2018_2628;
    }

    public void setOnCVE_2018_2628(boolean onCVE_2018_2628) {
        isOnCVE_2018_2628 = onCVE_2018_2628;
    }

    public boolean isOnCVE_2017_10271() {
        return isOnCVE_2017_10271;
    }

    public void setOnCVE_2017_10271(boolean onCVE_2017_10271) {
        isOnCVE_2017_10271 = onCVE_2017_10271;
    }

    public boolean isOnCVE_2017_3248() {
        return isOnCVE_2017_3248;
    }

    public void setOnCVE_2017_3248(boolean onCVE_2017_3248) {
        isOnCVE_2017_3248 = onCVE_2017_3248;
    }

    public boolean isOnCVE_2016_3510() {
        return isOnCVE_2016_3510;
    }

    public void setOnCVE_2016_3510(boolean onCVE_2016_3510) {
        isOnCVE_2016_3510 = onCVE_2016_3510;
    }

    public boolean isOnCVE_2016_0638() {
        return isOnCVE_2016_0638;
    }

    public void setOnCVE_2016_0638(boolean onCVE_2016_0638) {
        isOnCVE_2016_0638 = onCVE_2016_0638;
    }


    public boolean isIsonCVE_2020_2555() {
        return isonCVE_2020_2555;
    }

    public void setIsonCVE_2020_2555(boolean isonCVE_2020_2555) {
        this.isonCVE_2020_2555 = isonCVE_2020_2555;
    }

    public boolean isIsonCVE_2020_2551() {
        return isonCVE_2020_2551;
    }

    public void setIsonCVE_2020_2551(boolean isonCVE_2020_2551) {
        this.isonCVE_2020_2551 = isonCVE_2020_2551;
    }

    public boolean isIsonCVE_2019_2729() {
        return isonCVE_2019_2729;
    }

    public void setIsonCVE_2019_2729(boolean isonCVE_2019_2729) {
        this.isonCVE_2019_2729 = isonCVE_2019_2729;
    }
}
