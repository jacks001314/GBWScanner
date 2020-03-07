package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GBWScanYarnRestScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanYarnRestScript.class);
    private GBWScanYarnConfig config;

    public GBWScanYarnRestScript(GBWScanYarnConfig config){

        this.config = config;
    }

    public boolean scan(Host host, SinkQueue sinkQueue){

        boolean res = false;
        HttpUriRequest httpRequest;
        GBWHttpResponse httpResponse;

        CloseableHttpClient httpClient = null;

        try {

            httpClient = GBWHttpClientBuilder.make(host.getProto(),host.getPort());

            /*create a app*/
            GBWYarnApp app = GBWYarnUtils.createYarnApp(config,httpClient,host);
            if(!TextUtils.isEmpty(app.getAppId())){

                res = true;
                log.warn(String.format("Find a hadoop yarn create application bugs in %s:%d ,appID:%s",host.getServer(),host.getPort(),app.getAppId()));
                System.out.println(String.format("RestScript Find a hadoop yarn create application bugs in %s:%d ,appID:%s",host.getServer(),host.getPort(),app.getAppId()));

                GBWScanYarnResult result = new GBWScanYarnResult(config,host);
                result.setScan("Rest");
                result.setAppID(app.getAppId());

                GBWYarnCluster cluster = null;

                try {
                    cluster = GBWYarnUtils.getYarnCluster(config,httpClient,host);
                }catch (Exception e){

                }
                if(cluster!=null) {
                    GBWYarnClusterMetrics metrics = cluster.getClusterMetrics();

                    result.setMaxMem(metrics.getTotalMB());
                    result.setMaxVcores(metrics.getAvailableVirtualCores());
                    result.setNodes(metrics.getActiveNodes());
                }
                if(sinkQueue!=null)
                    sinkQueue.put(result);

                if(config.isRunCmd()){
                    httpRequest = GBWYarnUtils.createYarnAppSubmitRequest(config,httpClient,app,host);
                    HttpUtils.send(httpClient,httpRequest);
                }
            }

        }catch (Exception e){

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
