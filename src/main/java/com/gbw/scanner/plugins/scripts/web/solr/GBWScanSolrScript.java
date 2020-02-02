package com.gbw.scanner.plugins.scripts.web.solr;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.sink.SinkQueue;
import com.google.gson.Gson;
import com.xmap.api.utils.TextUtils;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public abstract class GBWScanSolrScript implements GBWScanScript {

    protected final GBWScanSolrScriptConfig config;

    public GBWScanSolrScript(GBWScanSolrScriptConfig config) {
        this.config = config;
    }

    public abstract void  processResult(SinkQueue sinkQueue, Host host, SolrCoreAdmin solrCoreAdmin, String result, String core);
    public abstract String runPoc(HttpClient httpClient, Host host, String core);

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }
    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        String result;
        HttpRequest httpRequest;
        HttpResponse httpResponse;
        String content;
        Gson gson = new Gson();

        HttpClient httpClient = HttpClient.newHttpClient();
        /**get solr admin cores info*/
        httpRequest = SolrHttpRequestBuilder.makeSolrCoreAdminRequest(host.getIp(),host.getPort(),config.getReadTimeout());
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if(httpResponse.statusCode()==200){

                content = (String)httpResponse.body();
                if(TextUtils.isEmpty(content)||!content.startsWith("{")){
                    /*not json text*/
                    return;
                }

                /*parseu json content**/
                SolrCoreAdmin solrCoreAdmin = gson.fromJson(content,SolrCoreAdmin.class);

                /*get all core names*/
                List<String> cores = solrCoreAdmin.getCoreNames();
                if(cores.isEmpty()){

                    cores = config.getDefaultCores();

                    if(cores == null||cores.isEmpty())
                        return;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
