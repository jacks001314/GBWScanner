package com.gbw.scanner.cmd;

public interface GBWCmdHandle {

    boolean isAccept(GBWCmdMessage message);
    void handle(GBWCmdMessage message);

}
