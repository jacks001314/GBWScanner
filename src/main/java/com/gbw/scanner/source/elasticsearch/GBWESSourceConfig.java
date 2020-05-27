package com.gbw.scanner.source.elasticsearch;

import com.gbw.scanner.elasticsearch.ESConfig;
import com.gbw.scanner.source.GBWHostSourceConfig;

import java.util.List;

public class GBWESSourceConfig extends GBWHostSourceConfig {

    private ESConfig esConfig;
    private long tv;
    private String statusFile;
    private String xmlPath;
    private List<GBWESSearchRule> rules;


    public List<GBWESSearchRule> getRules() {
        return this.rules;
    }

    public void setRules(List<GBWESSearchRule> rules) {
        this.rules = rules;
    }

    public ESConfig getEsConfig() {
        return this.esConfig;
    }

    public void setEsConfig(ESConfig esConfig) {
        this.esConfig = esConfig;
    }

    public long getTv() {
        return this.tv;
    }

    public void setTv(long tv) {
        this.tv = tv;
    }

    public String getStatusFile() {
        return this.statusFile;
    }

    public void setStatusFile(String statusFile) {
        this.statusFile = statusFile;
    }

    public String getXmlPath() {
        return this.xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }
}