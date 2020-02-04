package com.gbw.scanner.http;

import com.gbw.scanner.utils.SSLUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class GBWHttpClientBuilder {

    private GBWHttpClientBuilder(){

    }

    public static final CloseableHttpClient make(String proto,int port) throws Exception {

        CloseableHttpClient client;

        if(port == 443||(!TextUtils.isEmpty(proto)&&proto.equalsIgnoreCase("https")))
            client = SSLUtils.createClient();
        else
            client = HttpClients.createDefault();


        return client;
    }

}
