package com.gbw.scanner.plugins.scripts.web.tomcat;

public class GBWRecord {

    private final String name;
    private final String value;


    public GBWRecord(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
