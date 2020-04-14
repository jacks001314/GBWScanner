package com.gbw.scanner.plugins.scripts.hadoop.spark;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpPostRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.HttpUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GBWScanSparkRestScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanSparkRestScript.class);

    private GBWScanSparkConfig config;
    private static final String jsonFormat = "{\n" +
            "  \"action\": \"CreateSubmissionRequest\",\n" +
            "  \"clientSparkVersion\": \"%s\",\n" +
            "  \"appArgs\": [\n" +
            "    \"%s\"\n" +
            "  ],\n" +
            "  \"appResource\": \"%s\",\n" +
            "  \"environmentVariables\": {\n" +
            "    \"SPARK_ENV_LOADED\": \"1\"\n" +
            "  },\n" +
            "  \"mainClass\": \"%s\",\n" +
            "  \"sparkProperties\": {\n" +
            "    \"spark.jars\": \"%s\",\n" +
            "    \"spark.driver.supervise\": \"false\",\n" +
            "    \"spark.app.name\": \"%s\",\n" +
            "    \"spark.eventLog.enabled\": \"true\",\n" +
            "    \"spark.submit.deployMode\": \"cluster\",\n" +
            "    \"spark.master\": \"%s\"\n" +
            "  }\n" +
            "}";

    public GBWScanSparkRestScript(GBWScanSparkConfig config){

        this.config = config;
    }

    private HttpUriRequest makeAppRequest(Host host) throws IOException {

        String uri = "/v1/submissions/create";
        String sparkMatster = String.format("spark://%s:%d",host.getHost(),host.getPort());

        String postJson = String.format(jsonFormat,config.getSparkVersion(),
                config.getArgs(),
                config.getJarUrl(),
                config.getMainClass(),
                config.getJarUrl(),
                config.getAppName(),
                sparkMatster);

        return new GBWHttpPostRequestBuilder(host.getProto(),host.getHost(),host.getPort(),uri)
                .addHead("User-Agent",config.getUserAgent())
                .addHead("Accept"," application/json")
                .addHead("Content-Type"," application/json")
                .postString(postJson,false)
                .setTimeout(config.getConTimeout(),config.getReadTimeout())
                .build();
    }

    public String getResult(String host,int port,String subId){

        String uri = String.format("/logPage/?driverId=%s&logType=stdout",subId);

        GBWHttpResponse httpResponse;
        HttpUriRequest uriRequest;
        CloseableHttpClient httpClient = null;

        try {

            httpClient =  GBWHttpClientBuilder.make("http", port);
            uriRequest = new GBWHttpGetRequestBuilder("http",host,port,uri)
                    .addHead("User-Agent","ScanSpark")
                    .build();

            httpResponse = HttpUtils.send(httpClient,uriRequest,true);

            return httpResponse.getContent();

        }catch (Exception e){
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

        return "error";
    }

    public boolean scan(Host host, SinkQueue sinkQueue) {

        boolean res = false;

        CloseableHttpClient httpClient = null;
        GBWHttpResponse httpResponse;
        HttpUriRequest uriRequest;
        GBWSparkRestResponse restResponse;

        try {
           httpClient =  GBWHttpClientBuilder.make(host.getProto(), host.getPort());
           uriRequest = makeAppRequest(host);
           httpResponse = HttpUtils.send(httpClient,uriRequest,true);

           if(httpResponse.getStatus() == 200){

               restResponse = GBWSparkRestResponse.fromJson(httpResponse.getContent());

               if(restResponse.isSuccess()){

                   res = true;
                   /*find a val*/
                   String logStr = String.format("Find ok by spark restfull,address:%s:%d,response:%s",host.getHost(),host.getPort(),restResponse.toString());

                   if(sinkQueue!=null){

                       GBWScanSparkResult result = new GBWScanSparkResult(config,host,"Rest",restResponse);
                       sinkQueue.put(result);
                       log.warn(logStr);
                   }else{
                       System.out.println(logStr);
                   }
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
