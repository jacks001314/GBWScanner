package com.gbw.scanner.plugins.scripts.hadoop.spark;

import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;

public class GBWScanSparkConfig extends GBWScanScriptCommonConfig {

    private String sparkVersion;
    private String appName;
    private String jarFile;
    private String jarUrl;
    private String mainClass;
    private String args;

    private String userAgent;

    public String getJarFile() {
        return jarFile;
    }

    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }


    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getSparkVersion() {
        return sparkVersion;
    }

    public void setSparkVersion(String sparkVersion) {
        this.sparkVersion = sparkVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getJarUrl() {
        return jarUrl;
    }

    public void setJarUrl(String jarUrl) {
        this.jarUrl = jarUrl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
