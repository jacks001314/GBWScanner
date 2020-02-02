package com.gbw.scanner.plugins.webscan;

import java.net.http.HttpRequest;

public class GBWHttpRequest {

    private final HttpRequest httpRequest;
    private final boolean isBin;


    public GBWHttpRequest(HttpRequest httpRequest, boolean isBin) {
        this.httpRequest = httpRequest;
        this.isBin = isBin;
    }

    public boolean isBin() {
        return isBin;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

}
