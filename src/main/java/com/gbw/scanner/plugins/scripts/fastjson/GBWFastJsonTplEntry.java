package com.gbw.scanner.plugins.scripts.fastjson;

public class GBWFastJsonTplEntry {

    private String name;
    private String key;
    private boolean isCode;
    private boolean isCompress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isCode() {
        return isCode;
    }

    public void setCode(boolean code) {
        isCode = code;
    }

    public boolean isCompress() {
        return isCompress;
    }

    public void setCompress(boolean compress) {
        isCompress = compress;
    }
}
