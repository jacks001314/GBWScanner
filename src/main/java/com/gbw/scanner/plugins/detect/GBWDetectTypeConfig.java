package com.gbw.scanner.plugins.detect;

public class GBWDetectTypeConfig {

    private String name;
    private boolean isOn;
    private String cpath;

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

    public String getCpath() {
        return cpath;
    }

    public void setCpath(String cpath) {
        this.cpath = cpath;
    }
}
