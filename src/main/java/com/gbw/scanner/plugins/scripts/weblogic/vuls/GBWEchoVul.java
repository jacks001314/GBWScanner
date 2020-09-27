package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.Host;

public interface GBWEchoVul {

    GBWVulCheckResult scan(Host host);
    String exec(Host host,String pname,String cmd);
    void upload(Host host,String pname,String path, String text);
    void remove(Host host,String pname);
}
