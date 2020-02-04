package com.gbw.scanner.plugins.webscan;

public class GBWWebScanConfig {

    private int threads;
    private int conTimeout;
    private int readTimeout;

    private String ruleCPath;

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public String getRuleCPath() {
        return ruleCPath;
    }

    public void setRuleCPath(String ruleCPath) {
        this.ruleCPath = ruleCPath;
    }


    public int getConTimeout() {
        return conTimeout;
    }

    public void setConTimeout(int conTimeout) {
        this.conTimeout = conTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
