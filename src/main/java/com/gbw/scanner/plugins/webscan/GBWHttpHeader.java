package com.gbw.scanner.plugins.webscan;

public class GBWHttpHeader {

    private String name;
    private String value;

    public GBWHttpHeader(String name,String value){

        this.name = name;
        this.value = value;
    }

    public String toString(){

        return String.format("%s:%s",name,value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
