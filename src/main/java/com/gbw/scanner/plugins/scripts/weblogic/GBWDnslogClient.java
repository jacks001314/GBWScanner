package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public class GBWDnslogClient {

    private CloseableHttpClient httpClient;
    private String cookie = null;

    public GBWDnslogClient() throws Exception {

        httpClient = GBWHttpClientBuilder.make("http",80);
    }

    public void close(){

        try {
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GBWHttpResponse sendRequest(String uri,long timeout,String cookieValue){

        try {

            HttpGet httpGet;

            GBWHttpGetRequestBuilder builder = new GBWHttpGetRequestBuilder("http","www.dnslog.cn",80,uri)
                    .addHead("User-Agent","dnslog-client")
                    .setTimeout((int)timeout,(int)timeout);

            if(!TextUtils.isEmpty(cookieValue))
                    builder.addHead("Cookie",cookieValue);

            httpGet = builder.build();

            return HttpUtils.sendWithHeaders(httpClient,httpGet,true);

        }catch (Exception e){

        }

        return null;
    }

    public String getdomain(long timeout) throws Exception {

        String uri = "/getdomain.php";

        GBWHttpResponse response = sendRequest(uri,timeout,null);

        if(response!=null) {
            cookie = response.getHeaderMap().get("Set-Cookie").split(";")[0];
        }

        return response.getContent().trim();
    }

    public String getRecords(long timeout) throws Exception {
        String uri = "/getrecords.php";

        GBWHttpResponse response = sendRequest(uri,timeout,cookie);

        if(response!=null)
            return response.getContent().trim();

        return null;
    }

}
