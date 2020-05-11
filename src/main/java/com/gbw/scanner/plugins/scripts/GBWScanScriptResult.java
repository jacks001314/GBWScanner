package com.gbw.scanner.plugins.scripts;

import com.gbw.scanner.GBWScannerResult;
import com.gbw.scanner.Host;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public abstract class GBWScanScriptResult extends GBWScannerResult {

    private String scriptType;
    private String name;
    private String msg;
    private String subject;
    private String code;
    private String refer;

    public GBWScanScriptResult(GBWScanScriptCommonConfig scanScriptConfig, Host host,String type){

        setTime(System.currentTimeMillis());
        setHost(host.getHost());
        setIp(host.getIp());
        setPort(host.getPort());
        setType(type);

        setScriptType(scanScriptConfig.getType());
        setName(scanScriptConfig.getName());
        setMsg(scanScriptConfig.getMsg());
        setSubject(scanScriptConfig.getSubject());
        setCode(scanScriptConfig.getCode());
        setRefer(scanScriptConfig.getRefer());

        setDesc(msg);
        super.setCode(code);

    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cbb) throws IOException {

        cbb.field("scriptType",scriptType);
        cbb.field("name",name);
        cbb.field("msg",msg);
        cbb.field("subject",subject);
        cbb.field("code",code);
        cbb.field("refer",refer);

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
                "\"type\":{\"type\":\"keyword\"}," +
                "\"details\":{" +
                "\"properties\":{" +
                "\"scriptType\":{\"type\":\"keyword\"}," +
                "\"name\":{\"type\":\"keyword\"}," +
                "\"msg\":{\"type\":\"keyword\"}," +
                "\"subject\":{\"type\":\"keyword\"}," +
                "\"code\":{\"type\":\"keyword\"}," +
                "\"refer\":{\"type\":\"keyword\"}," +
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

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }
}
