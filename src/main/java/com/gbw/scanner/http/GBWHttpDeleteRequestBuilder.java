package com.gbw.scanner.http;

import com.gbw.scanner.utils.HttpUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;

public class GBWHttpDeleteRequestBuilder {

    private HttpDelete delete;

    public GBWHttpDeleteRequestBuilder(String proto, String host, int port, String uri){
         delete = new HttpDelete(HttpUtils.makeURI(proto,host,port,uri));
    }

    public GBWHttpDeleteRequestBuilder setTimeout(int conTimeout, int readTimeout){

        RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                .setSocketTimeout(readTimeout)
                .setConnectTimeout(conTimeout)
                .setConnectionRequestTimeout(conTimeout)
                .build();

        delete.setConfig(requestConfig);

        return this;
    }

    public GBWHttpDeleteRequestBuilder addHead(String key, String value){

        delete.addHeader(key,value);

        return this;
    }


    public HttpDelete build(){

        return delete;
    }

}
