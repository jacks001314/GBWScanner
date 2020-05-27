package com.gbw.scanner.source;

public class GBWHostSourcePoolConfigEntry {

    private boolean isOn;
    private String type;
    private String cpath;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
