package com.gbw.scanner.plugins.scripts.web.solr;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.HttpUtils;
import com.google.gson.Gson;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.List;

public abstract class GBWScanSolrScript implements GBWScanScript {

    protected final GBWScanSolrScriptConfig config;

    public GBWScanSolrScript(GBWScanSolrScriptConfig config) {
        this.config = config;
    }

    public abstract void  processResult(SinkQueue sinkQueue, Host host, SolrCoreAdmin solrCoreAdmin, String result, String core);
    public abstract String runPoc(CloseableHttpClient httpClient, Host host, String core);

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    private static  final boolean isJson(String content){

        return !TextUtils.isEmpty(content)&&content.startsWith("{");
    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        String result;
        HttpUriRequest httpRequest;
        GBWHttpResponse httpResponse;
        String content;
        Gson gson = new Gson();

        CloseableHttpClient httpClient = null;

        /**get solr admin cores info*/
        httpRequest = SolrHttpRequestBuilder.makeSolrCoreAdminRequest(host,config);
        try {
            httpClient = GBWHttpClientBuilder.make(host.getProto(),host.getPort());
            httpResponse = HttpUtils.send(httpClient,httpRequest,true);

            if(httpResponse.getStatus()==200){

                content = httpResponse.getContent();

                if(isJson(content)){
                    /*parseu json content**/
                    SolrCoreAdmin solrCoreAdmin = gson.fromJson(content,SolrCoreAdmin.class);
                    /*get all core names*/
                    List<String> cores = solrCoreAdmin.getCoreNames();
                    if(cores.isEmpty()){
                        cores = config.getDefaultCores();
                    }
                    /*try to detect for all cores until find a bug */
                    for(String core:cores){
                        result = runPoc(httpClient,host,core);
                        if(!TextUtils.isEmpty(result)){
                            processResult(sinkQueue,host,solrCoreAdmin,result,core);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
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
