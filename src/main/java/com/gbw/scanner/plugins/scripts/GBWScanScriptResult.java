package com.gbw.scanner.plugins.scripts;

import com.gbw.scanner.GBWScannerResult;
import com.gbw.scanner.Host;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class GBWScanScriptResult extends GBWScannerResult {


    public GBWScanScriptResult(GBWScanScriptCommonConfig scanScriptConfig, Host host,String type){

        setTime(System.currentTimeMillis());
        setProto(host.getProto());
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
    public  String getIndexMapping() {

        String mapping = "{" +
                "\"properties\":{" +
                "\"id\":{\"type\":\"keyword\"}," +
                "\"time\":{\"type\":\"long\"}," +
                "\"ip\":{\"type\":\"keyword\"}," +
                "\"port\":{\"type\":\"integer\"}," +
                "\"host\":{\"type\":\"keyword\"}," +
                "\"proto\":{\"type\":\"keyword\"}," +
                "\"scanType\":{\"type\":\"keyword\"}," +
                "\"code\":{\"type\":\"keyword\"}," +
                "\"type\":{\"type\":\"keyword\"},"+
                "\"desc\":{\"type\":\"keyword\"},"+
                "\"subject\":{\"type\":\"keyword\"},"+
                "\"details\":{" +
                "\"properties\":{" +
                "\"system\":{\"type\":\"keyword\"}," +
                "\"result\":{\"type\":\"keyword\"}," +
                "\"core\":{\"type\":\"keyword\"}," +
                "\"cmd\":{\"type\":\"keyword\"}," +
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
