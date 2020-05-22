package com.gbw.scanner.plugins.detect;

import com.gbw.scanner.GBWScannerResult;
import com.gbw.scanner.Host;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.script.Script;

import java.io.IOException;

public class GBWDetectResult extends GBWScannerResult {

    private final Host host;
    private final GBWDetectRule detectRule;

    private byte[] data;


    public GBWDetectResult(Host host, GBWDetectRule detectRule,String type) {

        this.host = host;
        this.detectRule = detectRule;

        setTime(System.currentTimeMillis());
        setProto(host.getProto());
        setHost(host.getHost());
        setIp(host.getIp());
        setPort(host.getPort());
        setScanType(type);

        setType(detectRule.getType());
        setDesc(detectRule.getMsg());
        setCode(detectRule.getType());
        setSubject(detectRule.getMsg());

    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public GBWDetectRule getDetectRule() {
        return detectRule;
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("ruleID",detectRule.getId());
        cb.field("proto",detectRule.getProto());
        cb.field("dataSize",data==null?0:data.length);
        cb.field("data",data==null?"".getBytes():data);

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
                "\"proto\":{\"type\":\"keyword\"}," +
                "\"data\":{\"type\":\"binary\"}," +
                "\"dataSize\":{\"type\":\"long\"}" +
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
        return "security_bug_detect";
    }

    @Override
    public String getIndexDocType()
    {
        return "SecurityBugDocDB";
    }

}
