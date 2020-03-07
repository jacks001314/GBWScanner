package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.commons.cli.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class GBWScanYarnScript implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanYarnScript.class);
    private GBWScanYarnConfig config;

    private final GBWScanYarnRestScript restScript;
    private final GBWScanYarnIPCScript ipcScript;

    public GBWScanYarnScript(GBWScanYarnConfig config){

        this.config = config;

        restScript = new GBWScanYarnRestScript(config);
        ipcScript = new GBWScanYarnIPCScript(config);
    }

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    private boolean isYarn(Host host, CloseableHttpClient httpClient) {

        List<String> keys = config.getKeys();
        if(keys == null || keys.size()==0)
            return false;

        HttpGet request = new GBWHttpGetRequestBuilder(host.getProto(),host.getServer(),host.getPort(),config.getUri())
                .addHead("User-Agent","YarnClient")
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .build();

        GBWHttpResponse response = HttpUtils.send(httpClient,request,true);

        String content = response.getContent();

        if(TextUtils.isEmpty(content))
            return false;


        for(String k:keys){

            if(!content.contains(k))
                return false;
        }

        return true;
    }

    @Override
    public boolean isAccept(Host host) {

        HttpUriRequest httpRequest;
        GBWHttpResponse httpResponse;
        boolean res = false;

        CloseableHttpClient httpClient = null;

        try {
            httpClient = GBWHttpClientBuilder.make(host.getProto(),host.getPort());
            res = isYarn(host,httpClient);
        }catch (Exception e){
            return false;
        }finally {

            if(httpClient!=null)
            {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return res;
    }



    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        boolean res = restScript.scan(host,sinkQueue);
        if(res){
            log.warn("RestScript Found!");
            System.out.println("RestScript Found!");
        }else{

            res = ipcScript.scan(host,sinkQueue);

            if(res){
                log.warn("IPCScript Found!");
                System.out.println("IPCScript Found!");
            }
        }
    }


    public static void main(String[] args) throws ParseException {

        GBWScanYarnConfig config = GBWYarnUtils.createYarnConfig(args);

        if(config!=null){

            System.out.println(config);

            Host host = new Host(config.getAddr(),config.getAddr(),8088,null,null);
            new GBWScanYarnScript(config).scan(host,null);
        }


    }

}
