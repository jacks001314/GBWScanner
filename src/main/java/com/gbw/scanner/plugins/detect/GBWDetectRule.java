package com.gbw.scanner.plugins.detect;

import java.util.List;

public class GBWDetectRule {

    private long id;
    private String type;
    private String msg;

    private String proto;
    private String connType;
    private boolean enable;
    private boolean isAnd;

    private List<GBWDetectRuleData> ruleDatas;

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isAnd() {
        return isAnd;
    }

    public void setAnd(boolean and) {
        isAnd = and;
    }

    public List<GBWDetectRuleData> getRuleDatas() {
        return ruleDatas;
    }

    public void setRuleDatas(List<GBWDetectRuleData> ruleDatas) {
        this.ruleDatas = ruleDatas;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getConnType() {
        return connType;
    }

    public void setConnType(String connType) {
        this.connType = connType;
    }
}
