package com.gbw.scanner.source.fofa;

import com.gbw.scanner.source.GBWHostSourceConfig;

import java.util.List;

public class GBWFoFaAPISourceConfig extends GBWHostSourceConfig {

    private String host;
    private String uri;
    private String email;
    private String key;
    private String qbase64;

    private List<String> scanType;
    private String proto;
    private int port;


    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQbase64() {
        return qbase64;
    }

    public void setQbase64(String qbase64) {
        this.qbase64 = qbase64;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public List<String> getScanType() {
        return scanType;
    }

    public void setScanType(List<String> scanType) {
        this.scanType = scanType;
    }
}
