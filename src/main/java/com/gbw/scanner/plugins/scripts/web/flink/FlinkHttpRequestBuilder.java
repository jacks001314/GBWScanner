package com.gbw.scanner.plugins.scripts.web.flink;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

public class FlinkHttpRequestBuilder {

    private static final String UA = "google bot";

    public static HttpRequest makeFirstPageRequest(String host, int port, int timeout){

        String uri = String.format("http://%s:%d",host,port);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .header("User-Agent",UA)
                .GET()
                .uri(URI.create(uri));


        return builder.build();
    }

    public static HttpRequest makeUPloadJarRequest(String host, int port, int timeout,String fname) throws FileNotFoundException {

        String multipartFormDataBoundary = "GoogleBOT+++++FormBoundary+++++++++++++";
        String uri = String.format("http://%s:%d/jars/upload",host,port);


        HttpEntity multipartEntity = MultipartEntityBuilder.create()
                .addPart("file", new FileBody(new File(fname), ContentType.DEFAULT_BINARY))
                .setBoundary(multipartFormDataBoundary) //要设置，否则阻塞
                .build();

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .header("User-Agent",UA)
                .header("Content-Type", "multipart/form-data; boundary=GoogleBOT+++++FormBoundary+++++++++++++")
                .header("Accept","application/json, text/plain, */*")
                .POST(HttpRequest.BodyPublishers.ofInputStream(() -> {
                    try {
                        return multipartEntity.getContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }))
                .uri(URI.create(uri));

        return builder.build();
    }

    public static HttpRequest makesubmitJarRequest(String host, int port, int timeout,String fname,String entryClass,String content) {

        String uri = String.format("http://%s:%d/jars/%s/run?entry-class=%s",host,port,fname,entryClass);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .header("User-Agent",UA)
                .header("Content-Type", "application/json")
                .header("Accept","application/json, text/plain, */*")
                .POST(HttpRequest.BodyPublishers.ofString(content))
                .uri(URI.create(uri));

        return builder.build();
    }

    public static HttpRequest makeDeleteJarRequest(String host, int port, int timeout,String fname){

        String uri = String.format("http://%s:%d/jars/%s",host,port,fname);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .timeout(Duration.ofMillis(timeout))
                .header("User-Agent",UA)
                .DELETE()
                .uri(URI.create(uri));

        return builder.build();
    }

}
