package com.gbw.scanner.source;

public interface GBWHostSource {


    void open() throws Exception;

    int read(GBWHostSourcePool sourcePool);

    void close();

    boolean isRemove();
}







