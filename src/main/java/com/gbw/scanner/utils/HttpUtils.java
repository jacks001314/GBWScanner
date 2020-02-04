package com.gbw.scanner.utils;

import com.gbw.scanner.http.GBWHttpResponse;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtils {

    private HttpUtils(){

    }

    public static final String makeURI(String proto,String host,int port,String uri){

        String base;

        if(TextUtils.isEmpty(proto)){

            proto = port == 443?"https":"http";
        }

        if(port == 80||port == 443)
            base = String.format("%s://%s",proto,host);
        else
            base = String.format("%s://%s:%d",proto,host,port);

        return uri.startsWith("/")?String.format("%s%s",base,uri):String.format("%s/%s",base,uri);
    }

    public static final GBWHttpResponse send(CloseableHttpClient closeableHttpClient, HttpUriRequest request,boolean isText){

        CloseableHttpResponse response = null;
        GBWHttpResponse textResponse = new GBWHttpResponse();

        try {

          response = closeableHttpClient.execute(request);

          textResponse.setStatus(response.getStatusLine().getStatusCode());

          if(isText)
              textResponse.setContent(EntityUtils.toString(response.getEntity()));
          else
              textResponse.setBytes(EntityUtils.toByteArray(response.getEntity()));

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return textResponse;
    }


    public static final GBWHttpResponse sendWithHeaders(CloseableHttpClient closeableHttpClient, HttpUriRequest request,boolean isText){

        CloseableHttpResponse response = null;
        GBWHttpResponse textResponse = new GBWHttpResponse();

        try {

            response = closeableHttpClient.execute(request);

            textResponse.setStatus(response.getStatusLine().getStatusCode());
            textResponse.setHeaders(response.getAllHeaders());

            if(isText)
                textResponse.setContent(EntityUtils.toString(response.getEntity()));
            else
                textResponse.setBytes(EntityUtils.toByteArray(response.getEntity()));

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return textResponse;
    }

    public static final void send(CloseableHttpClient closeableHttpClient, HttpUriRequest request) {

        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(request);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(response!=null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
