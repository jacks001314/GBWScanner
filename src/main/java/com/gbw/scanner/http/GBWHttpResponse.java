package com.gbw.scanner.http;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

public class GBWHttpResponse {

    private int status;
    private String content;
    private byte[] bytes;
    private Map<String,String> headerMap;

    public GBWHttpResponse(){

        status = 0;
        content = "";
        bytes = null;
        headerMap = new HashMap<>();
    }

    public void setHeaders(Header[] headers){

        if(headers!=null){

            for(Header header:headers){

                headerMap.put(header.getName(),header.getValue());
            }
        }
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
