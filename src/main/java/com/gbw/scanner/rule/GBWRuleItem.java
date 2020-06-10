package com.gbw.scanner.rule;

import com.gbw.scanner.plugins.detect.GBWDetectDataReader;
import com.gbw.scanner.plugins.detect.GBWDetectDataWriter;

public class GBWRuleItem {

    private String target;
    private String op;
    private String value;

    private boolean isArray;
    private boolean isBin;
    private boolean isnot;

    /*for tcp*/
    private GBWDetectDataWriter dataWriter;
    private GBWDetectDataReader dataReader;

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


    public boolean isIsnot() {
        return isnot;
    }

    public void setIsnot(boolean isnot) {
        this.isnot = isnot;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public boolean isBin() {
        return isBin;
    }

    public void setBin(boolean bin) {
        isBin = bin;
    }

    public GBWDetectDataWriter getDataWriter() {
        return dataWriter;
    }

    public void setDataWriter(GBWDetectDataWriter dataWriter) {
        this.dataWriter = dataWriter;
    }

    public GBWDetectDataReader getDataReader() {
        return dataReader;
    }

    public void setDataReader(GBWDetectDataReader dataReader) {
        this.dataReader = dataReader;
    }
}
