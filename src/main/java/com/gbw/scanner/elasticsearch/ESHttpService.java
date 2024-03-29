package com.gbw.scanner.elasticsearch;

import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpPostRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.utils.HttpUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

public class ESHttpService {

    public static String getMappings(String host,int port,String index) throws Exception{

        String url = String.format("/%s/_mappings?pretty=true",index);
        CloseableHttpClient httpClient = GBWHttpClientBuilder.make("http",port);


        HttpUriRequest request = new GBWHttpGetRequestBuilder("http",host,port,url)
                .addHead("User-Agent","esHttp")
                .build();

        GBWHttpResponse response = HttpUtils.send(httpClient,request,true);
        String res = response.getContent();
        httpClient.close();
        return res;
    }

    public static String get(String host,int port,String uri) throws Exception{


        CloseableHttpClient httpClient = GBWHttpClientBuilder.make("http",port);

        HttpUriRequest request = new GBWHttpGetRequestBuilder("http",host,port,uri)
                .addHead("User-Agent","esHttp")
                .build();

        GBWHttpResponse response = HttpUtils.send(httpClient,request,true);


        String res = response.getContent();
        httpClient.close();
        return res;
    }

    public static String post(String host,int port,String uri,String content,boolean isFile) throws Exception {

        CloseableHttpClient httpClient = GBWHttpClientBuilder.make("http",port);
        HttpUriRequest request = new GBWHttpPostRequestBuilder("http",host,port,uri)
                .addHead("User-Agent","esHttp")
                .addHead("Content-Type", "application/json")
                .postString(content,isFile)
                .build();

        GBWHttpResponse response = HttpUtils.send(httpClient,request,true);

        String res = response.getContent();
        httpClient.close();

        return res;
    }
}
