package com.gbw.scanner.http;

import com.gbw.scanner.utils.HttpUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

public class GBWHttpGetRequestBuilder {

    private HttpGet httpGet;

    public GBWHttpGetRequestBuilder(String proto,String host,int port,String uri){
        httpGet = new HttpGet(HttpUtils.makeURI(proto,host,port,uri));
    }

    public GBWHttpGetRequestBuilder setTimeout(int conTimeout,int readTimeout){

        RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                .setSocketTimeout(readTimeout)
                .setConnectTimeout(conTimeout)
                .setConnectionRequestTimeout(conTimeout)
                .build();

        httpGet.setConfig(requestConfig);

        return this;
    }

    public GBWHttpGetRequestBuilder addHead(String key,String value){

        httpGet.addHeader(key,value);

        return this;
    }


    public HttpGet build(){

        return httpGet;
    }

}
