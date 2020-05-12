package com.gbw.scanner.plugins.scripts;

import com.gbw.scanner.GBWScannerResult;
import com.gbw.scanner.Host;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public abstract class GBWScanScriptResult extends GBWScannerResult {


    public GBWScanScriptResult(GBWScanScriptCommonConfig scanScriptConfig, Host host,String type){

        setTime(System.currentTimeMillis());
        setHost(host.getHost());
        setIp(host.getIp());
        setPort(host.getPort());

        setScanType(type);
        setType(scanScriptConfig.getType());
        setDesc(scanScriptConfig.getMsg());
        setSubject(scanScriptConfig.getSubject());
        setCode(scanScriptConfig.getCode());

    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cbb) throws IOException {

        return cbb;
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
                "\"system\":{\"type\":\"keyword\"}," +
                "\"result\":{\"type\":\"keyword\"}," +
                "\"cores\":{\"type\":\"keyword\"}," +
                "\"os\":{\"type\":\"keyword\"}" +
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
        return "security_bug_script";
    }

    @Override
    public String getIndexDocType()
    {
        return "SecurityBugDocDB";
    }


}
