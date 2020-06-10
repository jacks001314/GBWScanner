package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpPostRequestBuilder;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GBWHttpRequestBuilder {

    private GBWHttpRequestBuilder(){

    }

    private static HttpUriRequest httpGet(Host host, GBWWebScanConfig config,String uri,GBWWebScanRule rule) {

        GBWHttpGetRequestBuilder builder =  new GBWHttpGetRequestBuilder(host.getProto(),host.getServer(),host.getPort(),uri)
                .setTimeout(config.getConTimeout(),config.getReadTimeout());

        rule.getHeaders().forEach(header->{
            builder.addHead(header.getName(),header.getValue());
        });

        return builder.build();
    }

    private static HttpUriRequest httpPost(Host host, GBWWebScanConfig config,String uri,GBWWebScanRule rule) throws IOException {

        GBWHttpPostRequestBuilder builder = new GBWHttpPostRequestBuilder(host.getProto(),host.getServer(),host.getPort(),uri)
                .setTimeout(config.getConTimeout(),config.getReadTimeout());

        rule.getHeaders().forEach(header->builder.addHead(header.getName(),header.getValue()));

        if(rule.isUpload()){

            builder.upload(rule.getPostArgs(),null,rule.getMime());
        }else{

            if(rule.isReqBodyText()){
                builder.postString(rule.getPostArgs(),false);
            }else{
                builder.postBytes(rule.getPostArgs(),false);
            }

        }

        return builder.build();
    }

    public static HttpUriRequest build(Host host, GBWWebScanConfig config,GBWWebScanRule rule) throws IOException {

        boolean isPost = rule.getMethod().equalsIgnoreCase("POST");

        HttpUriRequest request = null;

        String uri = rule.getUri();

        if(isPost){
            request = httpPost(host,config,uri,rule);
        }else{

            request = httpGet(host,config,uri,rule);
        }

        return request;
    }


}
