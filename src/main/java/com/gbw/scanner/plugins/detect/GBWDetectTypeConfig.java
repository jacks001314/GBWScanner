package com.gbw.scanner.plugins.detect;

public class GBWDetectTypeConfig {

    private String name;
    private boolean isOn;
    private String ruleDir;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public String getRuleDir() {
        return ruleDir;
    }

    public void setRuleDir(String ruleDir) {
        this.ruleDir = ruleDir;
    }
}
