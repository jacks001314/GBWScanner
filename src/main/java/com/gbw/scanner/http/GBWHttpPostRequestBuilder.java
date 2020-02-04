package com.gbw.scanner.http;

import com.gbw.scanner.utils.ByteDataUtils;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GBWHttpPostRequestBuilder {

    private HttpPost post;

    public GBWHttpPostRequestBuilder(String proto,String host,int port,String uri){
        post = new HttpPost(HttpUtils.makeURI(proto,host,port,uri));
    }

    public GBWHttpPostRequestBuilder setTimeout(int conTimeout,int readTimeout){

        RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                .setSocketTimeout(readTimeout)
                .setConnectTimeout(conTimeout)
                .setConnectionRequestTimeout(conTimeout)
                .build();

        post.setConfig(requestConfig);

        return this;
    }

    public GBWHttpPostRequestBuilder addHead(String key,String value){

        post.addHeader(key,value);

        return this;
    }

    public GBWHttpPostRequestBuilder postString(String content,boolean isFile) throws IOException {

        String rcontent = content;
        if(isFile)
            rcontent = new String(Files.readAllBytes(Paths.get(content)));

        StringEntity body = new StringEntity(rcontent,"utf-8");
        post.setEntity(body);
        return this;
    }

    public GBWHttpPostRequestBuilder postBytes(String hex,boolean isFile) throws IOException {

        byte[] rcontent;
        if(isFile)
            rcontent = Files.readAllBytes(Paths.get(hex));
        else
            rcontent = ByteDataUtils.parseHex(hex);

        HttpEntity httpEntity = new ByteArrayEntity(rcontent);
        post.setEntity(httpEntity);

        return this;
    }

    public GBWHttpPostRequestBuilder upload(String file,String boundary,ContentType contentType){

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addPart("file",new FileBody(new File(file), contentType));

        if(!TextUtils.isEmpty(boundary))
            multipartEntityBuilder.setBoundary(boundary);


        post.setEntity(multipartEntityBuilder.build());

        return this;
    }

    public GBWHttpPostRequestBuilder upload(String file,String boundary,String mimeType){

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

        ContentType contentType = ContentType.getByMimeType(mimeType);
        if(contentType!=null)
            multipartEntityBuilder.addPart("file",new FileBody(new File(file), contentType));
        else
            multipartEntityBuilder.addPart("file",new FileBody(new File(file), mimeType));

        if(!TextUtils.isEmpty(boundary))
            multipartEntityBuilder.setBoundary(boundary);

        post.setEntity(multipartEntityBuilder.build());

        return this;
    }


    public HttpPost build(){

        return post;
    }


}
