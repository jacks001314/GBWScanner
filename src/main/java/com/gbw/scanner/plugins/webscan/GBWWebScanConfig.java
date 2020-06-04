package com.gbw.scanner.plugins.webscan;

public class GBWWebScanConfig {

    private int threads;
    private int conTimeout;
    private int readTimeout;

    private String ruleDir;

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
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

    public String getRuleDir() {
        return ruleDir;
    }

    public void setRuleDir(String ruleDir) {
        this.ruleDir = ruleDir;
    }
}
