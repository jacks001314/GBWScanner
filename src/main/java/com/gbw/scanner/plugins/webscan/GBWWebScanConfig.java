package com.gbw.scanner.plugins.webscan;

public class GBWWebScanConfig {

    private int threads;
    private long timeout;

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

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
