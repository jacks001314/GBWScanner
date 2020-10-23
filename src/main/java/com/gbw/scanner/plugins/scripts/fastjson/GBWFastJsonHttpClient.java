package com.gbw.scanner.plugins.scripts.fastjson;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpPostRequestBuilder;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GBWFastJsonHttpClient {

    public static void send(Host host,GBWFastJsonURL url,String json){

        String content = json;
        Map<String,String> headers = new HashMap<>();

        if(!TextUtils.isEmpty(url.getCookie())){

            headers.put("Cookie",url.getCookie());
        }

        if(!TextUtils.isEmpty(url.getParams())){
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            content = String.format(url.getParams(),json);
        }

        CloseableHttpClient httpClient = null;

        try {

            httpClient = GBWHttpClientBuilder.make(host.getProto(),host.getPort());
            HttpPost httpPost = new GBWHttpPostRequestBuilder(host.getProto(),host.getHost(),host.getPort(),url.getUri())
                    .addHeaders(headers)
                    .postString(content,false)
                    .build();

            HttpUtils.send(httpClient,httpPost);

        }catch (Exception e){

            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException ioException) {

                }
            }
        }
    }
}
