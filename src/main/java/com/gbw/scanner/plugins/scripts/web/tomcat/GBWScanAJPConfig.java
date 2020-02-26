package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;

public class GBWScanAJPConfig extends GBWScanScriptCommonConfig {

    private String uri;
    private String file;

    private String storeDir;
    private String filePrefix;

    private boolean isSSL;

    public boolean isSSL() {
        return isSSL;
    }

    public void setSSL(boolean SSL) {
        isSSL = SSL;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }



    public String getStoreDir() {
        return storeDir;
    }

    public void setStoreDir(String storeDir) {
        this.storeDir = storeDir;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }
}
