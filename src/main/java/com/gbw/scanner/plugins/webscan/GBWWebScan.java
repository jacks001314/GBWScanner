package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.Host;
import com.gbw.scanner.sink.SinkQueue;

import java.util.List;

public interface GBWWebScan {

    void scan(Host host, GBWWebScanRule scanRule,SinkQueue sinkQueue);

    GBWWebScanRuleConfig getScanRuleConfig();
}
