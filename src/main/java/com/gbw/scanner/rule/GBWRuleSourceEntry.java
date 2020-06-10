package com.gbw.scanner.rule;

public interface GBWRuleSourceEntry {

    boolean canMatch(String proto);
    String getTargetValue(GBWRuleItem item);

}
