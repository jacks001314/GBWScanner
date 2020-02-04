package com.gbw.scanner.plugins.scripts.web.flink;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpDeleteRequestBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpPostRequestBuilder;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

import java.io.IOException;

public class FlinkHttpRequestBuilder {

    private static final String UA = "google bot";

    public static HttpGet makeFirstPageRequest(Host host,GBWScanFlinkScriptConfig config){

        return new GBWHttpGetRequestBuilder(host.getProto(),host.getServer(),host.getPort(),"/")
                .addHead("User-Agent",UA)
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .build();
    }

    public static HttpPost makeUPloadJarRequest(Host host,GBWScanFlinkScriptConfig config) throws Exception {

        return new GBWHttpPostRequestBuilder(host.getProto(),host.getServer(),host.getPort(),"/jars/upload")
                .addHead("User-Agent",UA)
                .addHead("Content-Type","multipart/form-data; boundary=GoogleBOT+++++FormBoundary+++++++++++++")
                .addHead("Accept","application/json, text/plain, */*")
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .upload(config.getJarFile(),"GoogleBOT+++++FormBoundary+++++++++++++",ContentType.DEFAULT_BINARY)
                .build();
    }

    public static HttpPost makesubmitJarRequest(Host host,GBWScanFlinkScriptConfig config,String fname) throws IOException {

        return new GBWHttpPostRequestBuilder(host.getProto(),host.getServer(),host.getPort(),
                String.format("/jars/%s/run?entry-class=%s",fname,config.getEntryClass()))
                .addHead("User-Agent",UA)
                .addHead("Content-Type", "application/json")
                .addHead("Accept","application/json, text/plain, */*")
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .postString(config.getSubmitJson(),false)
                .build();
    }

    public static HttpDelete makeDeleteJarRequest(Host host,GBWScanFlinkScriptConfig config,String fname) {

        return new GBWHttpDeleteRequestBuilder(host.getProto(),host.getServer(),host.getPort(),
                String.format("/jars/%s",fname))
                .addHead("User-Agent",UA)
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .build();
    }

}
