package com.gbw.scanner.plugins.detect;

public class GBWDetectDataWriter {

    private boolean isBin;
    private String data;

    public boolean isBin() {
        return isBin;
    }

    public void setBin(boolean bin) {
        isBin = bin;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
