package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.GBWScannerResult;
import com.gbw.scanner.Host;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWWebScanResult extends GBWScannerResult {

    private long ruleID;


    public GBWWebScanResult(Host host, GBWWebScanRule scanRule){


        setTime(System.currentTimeMillis());
        setHost(host.getHost());
        setIp(host.getIp());
        setPort(host.getPort());
        setScanType("webScan");

        setRuleID(scanRule.getId());
        setType(scanRule.getType());
        setDesc(scanRule.getMsg());

        setSubject(scanRule.getMsg());
        setDesc(scanRule.getMsg());
        setCode(scanRule.getType());
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("ruleID",ruleID);

        return cb;
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
                "\"scanType\":{\"type\":\"keyword\"}," +
                "\"code\":{\"type\":\"keyword\"}," +
                "\"type\":{\"type\":\"keyword\"},"+
                "\"desc\":{\"type\":\"keyword\"},"+
                "\"subject\":{\"type\":\"keyword\"},"+
                "\"details\":{" +
                "\"properties\":{" +
                "\"ruleID\":{\"type\":\"keyword\"}," +
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

}
