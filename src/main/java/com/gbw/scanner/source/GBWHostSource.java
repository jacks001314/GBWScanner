package com.gbw.scanner.source;

public interface GBWHostSource {

    public static final String fileLine = "fLine";
    public static final String es = "es";
    public static final String shodan = "shodan";
    public static final String fofa = "fofa";
    public static final String ipRange = "ipRange";

    void open() throws Exception;

    int read(GBWHostSourcePool sourcePool);

    void close();

    boolean isRemove();

    boolean isTimeout(long curTime);

    void reopen(long curTime) throws Exception;
}







