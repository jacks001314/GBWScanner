package com.gbw.scanner.plugins.detect;

import com.gbw.scanner.Host;

public interface GBWDetect {

    GBWDetectResult detect(Host host,GBWDetectRule rule) throws GBWDetectException;

    GBWDetectRuleConfig getRuleConfig();
}
