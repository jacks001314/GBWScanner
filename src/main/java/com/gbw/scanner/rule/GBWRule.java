package com.gbw.scanner.rule;

import java.util.List;

public class GBWRule {

    private long id;
    private String type;
    private String msg;

    private boolean enable;
    private boolean isAnd;

    private List<GBWRuleItem> items;


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

    public boolean isAnd() {
        return isAnd;
    }

    public void setAnd(boolean and) {
        isAnd = and;
    }


    public List<GBWRuleItem> getItems() {
        return items;
    }

    public void setItems(List<GBWRuleItem> items) {
        this.items = items;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
