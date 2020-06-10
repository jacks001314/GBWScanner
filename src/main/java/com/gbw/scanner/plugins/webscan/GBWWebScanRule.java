package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.rule.GBWRule;
import java.util.List;

public class GBWWebScanRule extends GBWRule {


    private boolean isReqBodyText;
    private boolean isResBodyText;

    private String proto;
    private String method;
    private String uri;

    private boolean isUpload;
    private String mime;

    private String postArgs;

    private List<GBWHttpHeader> headers;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public String getPostArgs() {
        return postArgs;
    }

    public void setPostArgs(String postArgs) {
        this.postArgs = postArgs;
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


    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }


    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isReqBodyText() {
        return isReqBodyText;
    }

    public void setReqBodyText(boolean reqBodyText) {
        isReqBodyText = reqBodyText;
    }

    public boolean isResBodyText() {
        return isResBodyText;
    }

    public void setResBodyText(boolean resBodyText) {
        isResBodyText = resBodyText;
    }
}
