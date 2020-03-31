package com.gbw.scanner.plugins.scripts.web.solr.dataimport;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.plugins.scripts.web.solr.GBWScanSolrScript;
import com.gbw.scanner.plugins.scripts.web.solr.GBWScanSolrScriptConfig;
import com.gbw.scanner.plugins.scripts.web.solr.SolrCoreAdmin;
import com.gbw.scanner.plugins.scripts.web.solr.SolrHttpRequestBuilder;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWScanSolrDataImportScript extends GBWScanSolrScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanSolrDataImportScript.class);

    public GBWScanSolrDataImportScript(GBWScanSolrScriptConfig config) {
        super(config);
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }


    public String runPoc(CloseableHttpClient httpClient, Host host, String core){

        GBWHttpResponse httpResponse;
        HttpUriRequest httpRequest;

        try {
            httpRequest = SolrHttpRequestBuilder.makeSolrDataimportPocRequest(host,config,core);
            httpResponse = HttpUtils.send(httpClient,httpRequest,true);

            if(httpResponse.getStatus() == 200){

                String text = httpResponse.getContent();
                if(!TextUtils.isEmpty(text)&&text.contains("Requests"))
                    return text;
                else
                    return null;
            }

        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }

        return null;
    }

    public void processResult(SinkQueue sinkQueue, Host host, SolrCoreAdmin solrCoreAdmin, String result,String core){

        if(sinkQueue!=null){
            GBWScanSolrDataImportScriptResult scriptResult = new GBWScanSolrDataImportScriptResult(config,host,result,solrCoreAdmin,core);
            sinkQueue.put(scriptResult);
        }else{
            System.out.println(String.format("Find a solr dataimport source bugs in Host:%s:%d",host.getServer(),host.getPort()));
            System.out.println("cmd:"+config.getCmd());
            System.out.println(result);
        }

        log.warn(String.format("Find a solr dataimport source bugs in Host:%s:%d",host.getServer(),host.getPort()));
    }

    public static void main(String[] args) throws Exception{

        GBWScanSolrScriptConfig config = new GBWScanSolrScriptConfig();
        GBWScanSolrDataImportScript scanSolrDataImportScript = new GBWScanSolrDataImportScript(config);

        scanSolrDataImportScript.runMain(args);
    }
}
