package com.gbw.scanner.plugins.scripts.fastjson;

public class GBWFastJsonURL {

    private String uri;
    private String params;
    private String cookie;

    public GBWFastJsonURL(String uri,String params){

        this.uri = uri;
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
