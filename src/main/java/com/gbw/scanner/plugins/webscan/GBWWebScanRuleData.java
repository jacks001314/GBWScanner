package com.gbw.scanner.plugins.webscan;

public class GBWWebScanRuleData {

    private String target;
    private String op;
    private String preOP;
    private boolean isBin;

    private String value;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isBin() {
        return isBin;
    }

    public void setBin(boolean bin) {
        isBin = bin;
    }

    public String getPreOP() {
        return preOP;
    }

    public void setPreOP(String preOP) {
        this.preOP = preOP;
    }
}
