package com.gbw.scanner.plugins.scripts.web.solr.dataimport;

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

public class GBWScanSolrDataImportScript extends GBWScanSolrScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanSolrDataImportScript.class);

    public GBWScanSolrDataImportScript(GBWScanSolrScriptConfig config) {
        super(config);
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }


    public String runPoc(HttpClient httpClient, Host host, String core){

        HttpResponse httpResponse;
        HttpRequest httpRequest = SolrHttpRequestBuilder.makeSolrDataimportPocRequest(host.getServer(),host.getPort(),core,config.getCmd(),config.isEncode(),config.getConTimeout());
        try {

            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if(httpResponse.statusCode() == 200){

                String text = (String)httpResponse.body();
                if(text.contains("Requests"))
                    return text;
                else
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void processResult(SinkQueue sinkQueue, Host host, SolrCoreAdmin solrCoreAdmin, String result,String core){

        GBWScanSolrDataImportScriptResult scriptResult = new GBWScanSolrDataImportScriptResult(config,host,result,solrCoreAdmin,core);
        sinkQueue.put(scriptResult);
        log.warn(String.format("Find a solr dataimport source bugs in Host:%s:%d",host.getServer(),host.getPort()));
    }

}
