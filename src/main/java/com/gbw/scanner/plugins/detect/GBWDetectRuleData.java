package com.gbw.scanner.plugins.detect;

public class GBWDetectRuleData {

    private boolean isBin;


    private String rawData;
    private String op;
    private String preOP;

    private GBWDetectDataWriter dataWriter;
    private GBWDetectDataReader dataReader;


    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public boolean isBin() {
        return isBin;
    }

    public void setBin(boolean bin) {
        isBin = bin;
    }

    public GBWDetectDataReader getDataReader() {
        return dataReader;
    }

    public void setDataReader(GBWDetectDataReader dataReader) {
        this.dataReader = dataReader;
    }

    public GBWDetectDataWriter getDataWriter() {
        return dataWriter;
    }

    public void setDataWriter(GBWDetectDataWriter dataWriter) {
        this.dataWriter = dataWriter;
    }

    public String getPreOP() {
        return preOP;
    }

    public void setPreOP(String preOP) {
        this.preOP = preOP;
    }
}
