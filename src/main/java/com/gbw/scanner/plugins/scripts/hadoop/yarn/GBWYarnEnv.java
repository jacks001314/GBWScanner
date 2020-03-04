package com.gbw.scanner.plugins.scripts.hadoop.yarn;

public class GBWYarnEnv {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString(){

        return String.format("%s=%s",key,value);
    }

}
