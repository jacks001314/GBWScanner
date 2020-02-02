package com.gbw.scanner.plugins.bruteforce;

import com.gbw.scanner.Host;

public interface GBWBruteForce {

    String getProto(Host host);
    GBWDict getDict();
    GBWBruteForceResult bruteForce(Host host, GBWDictEntry entry);
}
