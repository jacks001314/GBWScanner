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

            builder.upload(rule.getPostArgsFilePath(),null,rule.getMime());
        }else{

            if(rule.isText()){
                String file = rule.getPostArgsFilePath();
                boolean isFile = !TextUtils.isEmpty(file);
                builder.postString(isFile?file:rule.getPostArgs(),isFile);
            }else{
                /*bytes*/
                String file = rule.getPostArgsFilePath();
                boolean isFile = !TextUtils.isEmpty(file);

                builder.postBytes(isFile?file:rule.getPostArgs(),isFile);
            }

        }

        return builder.build();
    }

    public static List<HttpUriRequest> build(Host host, GBWWebScanConfig config,GBWWebScanRule rule) throws IOException {

        List<HttpUriRequest> httpRequests = new ArrayList<>();

        List<String> uris = rule.getUris();
        boolean isPost = rule.getMethod().equalsIgnoreCase("POST");

        HttpUriRequest request = null;

        for(String u:uris){

            if(isPost){

                request = httpPost(host,config,u,rule);
            }else{

                request = httpGet(host,config,u,rule);
            }
            if(request!=null){

                httpRequests.add(request);
            }
        }

        return httpRequests;
    }


}
