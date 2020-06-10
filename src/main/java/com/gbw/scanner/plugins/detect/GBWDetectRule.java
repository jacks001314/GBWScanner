package com.gbw.scanner.plugins.detect;

import com.gbw.scanner.rule.GBWRule;

public class GBWDetectRule extends GBWRule {

    private String proto;
    private String connType;

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }


    public String getConnType() {
        return connType;
    }

    public void setConnType(String connType) {
        this.connType = connType;
    }
}
