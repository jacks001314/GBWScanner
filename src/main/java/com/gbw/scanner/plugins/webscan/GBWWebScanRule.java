package com.gbw.scanner.plugins.webscan;

import com.xmap.api.utils.TextUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GBWWebScanRule {

    private boolean enable;
    private long id;
    private String type;
    private String msg;
    private boolean isAnd;
    private boolean isBin;

    private String proto;
    private String method;
    private String uriFilePath;
    private List<String> uris;
    private String postArgs;
    private String postArgsFilePath;

    private List<GBWHttpHeader> headers;
    private List<GBWWebScanRuleData> matches;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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

    public boolean isAnd() {
        return isAnd;
    }

    public void setAnd(boolean and) {
        isAnd = and;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUriFilePath() {
        return uriFilePath;
    }

    public void setUriFilePath(String uriFilePath) {
        this.uriFilePath = uriFilePath;
        if(!TextUtils.isEmpty(uriFilePath)){

            try {
                List<String> uriLines = Files.readAllLines(Paths.get(uriFilePath));
                if(uris == null) {
                    uris = uriLines;
                }
                else{

                    uris.addAll(uriLines);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public String getPostArgs() {
        return postArgs;
    }

    public void setPostArgs(String postArgs) {
        this.postArgs = postArgs;
    }

    public String getPostArgsFilePath() {
        return postArgsFilePath;
    }

    public void setPostArgsFilePath(String postArgsFilePath) {
        this.postArgsFilePath = postArgsFilePath;
    }

    public List<GBWHttpHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<GBWHttpHeader> headers) {
        this.headers = headers;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public List<GBWWebScanRuleData> getMatches() {
        return matches;
    }

    public void setMatches(List<GBWWebScanRuleData> matches) {
        this.matches = matches;
    }

    public boolean isBin() {
        return isBin;
    }

    public void setBin(boolean bin) {
        isBin = bin;
    }
}
