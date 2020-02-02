package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.GBWScannerResult;
import com.gbw.scanner.Host;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GBWWebScanResult extends GBWScannerResult {

    private String method;
    private String uri;

    private long ruleID;
    private String ruleType;
    private String ruleMsg;


    private List<GBWHttpHeader> reqHeaders;

    private int status;
    private List<GBWHttpHeader> resHeaders;


    public GBWWebScanResult(Host host, HttpResponse response, GBWWebScanRule scanRule){


        setTime(System.currentTimeMillis());
        setHost(host.getHost());
        setIp(host.getIp());
        setPort(host.getPort());
        setType("webScan");

        setMethod(response.request().method());
        setUri(response.uri().toString());
        reqHeaders = new ArrayList<>();
        setHeaders(reqHeaders,response.request().headers());
        setStatus(response.statusCode());

        resHeaders = new ArrayList<>();
        setHeaders(resHeaders,response.headers());

        setRuleID(scanRule.getId());
        setRuleType(scanRule.getType());
        setRuleMsg(scanRule.getMsg());

    }

    public GBWWebScanResult(HttpResponse response){

        HttpRequest httpRequest = response.request();
        HttpHeaders httpReqHeaders = httpRequest.headers();

        setTime(System.currentTimeMillis());

        setHost(httpReqHeaders.firstValue("GBWHost").get());
        setIp(httpReqHeaders.firstValue("GBWIP").get());
        setPort(Integer.parseInt(httpReqHeaders.firstValue("GBWPort").get()));
        setType(httpReqHeaders.firstValue("GBWScanType").get());

        setMethod(response.request().method());
        setUri(response.uri().toString());

        reqHeaders = new ArrayList<>();
        setHeaders(reqHeaders,response.request().headers());
        setStatus(response.statusCode());

        resHeaders = new ArrayList<>();
        setHeaders(resHeaders,response.headers());

        setRuleID(Long.parseLong(httpReqHeaders.firstValue("GBRRuleID").get()));
        setRuleType(httpReqHeaders.firstValue("GBWRuleType").get());
        setRuleMsg(httpReqHeaders.firstValue("GBWRuleMsg").get());

    }

    private void setHeaders(List<GBWHttpHeader> headers, HttpHeaders httpHeaders){

        for(Map.Entry<String,List<String>> entry:httpHeaders.map().entrySet()){

            String key = entry.getKey();
            List<String> values = entry.getValue();
            for(String value:values){

                GBWHttpHeader httpHeader = new GBWHttpHeader(key,value);
                headers.add(httpHeader);
            }
        }
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("ruleID",ruleID);
        cb.field("ruleType",ruleType);
        cb.field("ruleMsg",ruleMsg);
        cb.field("method",method);
        cb.field("uri",uri);
        cb.field("status",status);
        cb.field("reqHeaders",reqHeaders);
        cb.field("resHeaders",resHeaders);

        return cb;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<GBWHttpHeader> getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(List<GBWHttpHeader> reqHeaders) {
        this.reqHeaders = reqHeaders;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<GBWHttpHeader> getResHeaders() {
        return resHeaders;
    }

    public void setResHeaders(List<GBWHttpHeader> resHeaders) {
        this.resHeaders = resHeaders;
    }

    @Override
    public String getIndexMapping() {
        String mapping = "{" +
                "\"properties\":{" +
                "\"id\":{\"type\":\"keyword\"}," +
                "\"time\":{\"type\":\"long\"}," +
                "\"ip\":{\"type\":\"keyword\"}," +
                "\"port\":{\"type\":\"integer\"}," +
                "\"host\":{\"type\":\"keyword\"}," +
                "\"type\":{\"type\":\"keyword\"}," +
                "\"details\":{" +
                "\"properties\":{" +
                "\"ruleID\":{\"type\":\"keyword\"}," +
                "\"ruleType\":{\"type\":\"keyword\"}," +
                "\"ruleMsg\":{\"type\":\"keyword\"}," +
                "\"method\":{\"type\":\"keyword\"}," +
                "\"uri\":{\"type\":\"keyword\"}," +
                "\"reqHeaders\":{\"type\":\"keyword\"}," +
                "\"resHeaders\":{\"type\":\"keyword\"}" +
                "}" +
                "}," +
                "\"srcIPLocation\":{" +
                "\"properties\":{" +
                "\"location\":{\"type\":\"keyword\"}," +
                "\"country\":{\"type\":\"keyword\"}," +
                "\"city\":{\"type\":\"keyword\"}," +
                "\"longitude\":{\"type\":\"double\"}," +
                "\"latitude\":{\"type\":\"double\"}" +
                "}" +
                "}," +
                "\"dstIPLocation\":{" +
                "\"properties\":{" +
                "\"location\":{\"type\":\"keyword\"}," +
                "\"country\":{\"type\":\"keyword\"}," +
                "\"city\":{\"type\":\"keyword\"}," +
                "\"longitude\":{\"type\":\"double\"}," +
                "\"latitude\":{\"type\":\"double\"}" +
                "}" +
                "}" +
                "}" +
                "}";

        return mapping;
    }

    @Override
    public String getIndexNamePrefix() {
        return "security_bug_webscan";
    }

    @Override
    public String getIndexDocType()
    {
        return "SecurityBugDocDB";
    }


    public long getRuleID() {
        return ruleID;
    }

    public void setRuleID(long ruleID) {
        this.ruleID = ruleID;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleMsg() {
        return ruleMsg;
    }

    public void setRuleMsg(String ruleMsg) {
        this.ruleMsg = ruleMsg;
    }
}
