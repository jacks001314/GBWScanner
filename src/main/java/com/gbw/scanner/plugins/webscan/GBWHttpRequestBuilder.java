package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.Host;
import com.xmap.api.utils.TextUtils;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GBWHttpRequestBuilder {

    private GBWHttpRequestBuilder(){

    }

    private static HttpRequest httpGet(String uri,GBWWebScanRule rule,long timeout) {

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .GET()
                .uri(URI.create(uri));

        rule.getHeaders().forEach(header->{
            builder.header(header.getName(),header.getValue());
        });

        return builder.build();
    }

    private static HttpRequest httpPost(String uri,GBWWebScanRule rule,long timeout){

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .uri(URI.create(uri));
        if(TextUtils.isEmpty(rule.getPostArgsFilePath())){
            builder.POST(HttpRequest.BodyPublishers.ofString(rule.getPostArgs()));
        }else{
            try {
                builder.POST(HttpRequest.BodyPublishers.ofFile(Paths.get(rule.getPostArgsFilePath())));
            } catch (FileNotFoundException e) {
                return null;
            }
        }

        rule.getHeaders().forEach(header->{
            builder.header(header.getName(),header.getValue());
        });

        return builder.build();
    }

    public static List<HttpRequest> build(Host host, GBWWebScanRule rule,long timeout) {

        String h = String.format("%s://%s",rule.getProto(),host.getServer());
        if(host.getPort()!=80&&host.getPort()!=443)
            h = h+":"+host.getPort();

        List<HttpRequest> httpRequests = new ArrayList<>();

        List<String> uris = rule.getUris();
        boolean isPost = rule.getMethod().equalsIgnoreCase("POST");

        HttpRequest request = null;

        for(String u:uris){

            String uri = String.format("%s%s",h,u);
            if(isPost){

                request = httpPost(uri,rule,timeout);
            }else{

                request = httpGet(uri,rule,timeout);
            }
            if(request!=null){

                httpRequests.add(request);
            }
        }

        return httpRequests;
    }


}
