package com.gbw.scanner;


public interface GBWScannerPlugin {

    void start();

    void stop();

    boolean accept(Host host);

    void process(Host host);

}
