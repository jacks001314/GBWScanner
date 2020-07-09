package com.gbw.scanner.source.fofa;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.source.GBWHostSource;
import com.gbw.scanner.source.GBWHostSourcePool;
import com.gbw.scanner.utils.GsonUtils;
import com.gbw.scanner.utils.HttpUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class GBWFoFaAPISource implements GBWHostSource {

    private static final Logger log = LoggerFactory.getLogger(GBWFoFaAPISource.class);

    private GBWFoFaAPISourceConfig config;
    private CloseableHttpClient httpClient;
    private int page;
    private int size;

    public GBWFoFaAPISource(GBWFoFaAPISourceConfig config){

        this.config = config;
        this.page = 1;
        this.size = 100;
    }

    @Override
    public void open() throws Exception {
        httpClient = GBWHttpClientBuilder.make("https",443);
    }

    private GBWFoFaData getData(){

        String uri = String.format("%s?email=%s&key=%s&qbase64=%s&page=%d&size=%d",config.getUri(),
                config.getEmail(),config.getKey(),config.getQbase64(),page,size);

        HttpGet request = new GBWHttpGetRequestBuilder("https",config.getHost(),443,uri)
                .addHead("User-Agent","GBWScanner")
                .build();

        try {

            GBWHttpResponse httpResponse = HttpUtils.send(httpClient,request,true);

            GBWFoFaData foFaData = GsonUtils.loadConfigFromJson(httpResponse.getContent(),GBWFoFaData.class);

            if(foFaData!=null){
                if(foFaData.isError()){

                    log.error(String.format("Get FoFa Data failed,uri:%s,content:%s",uri,httpResponse.getContent()));
                }else{
                    log.info(String.format("Get FoFa Data ok,uri:%s,page:%d,size:%d,results:%d",uri,
                            page,size,foFaData.getResults().size()));
                }
            }

            return foFaData;

        }catch (Exception e){

            log.error(String.format("Get FoFa data failed,uri:%s,error:%s",uri,e.getMessage()));
        }

        return null;
    }


    @Override
    public int read(GBWHostSourcePool sourcePool) {

        int count = 0;

        GBWFoFaData foFaData = getData();

        if(foFaData == null ||foFaData.isError()||foFaData.getResults() == null||foFaData.getResults().size()==0)
            return 0;


        for(List<String> res:foFaData.getResults()){

            if(res.size()==0){
                log.error("Invalid result:"+res.size());
                continue;
            }

            GBWFoFaHostData hostData = new GBWFoFaHostData(res.get(0),config.getProto(),config.getPort());

            Host host = new Host(hostData.getHost(),hostData.getHost(),hostData.getPort(),config.getScanType(),hostData.getProto());

            sourcePool.put(host);

            count++;
        }

        page++;

        return count;
    }

    @Override
    public void close() {

        try {
            log.info("close fafa client!");
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRemove() {
        return true;
    }

    @Override
    public boolean isTimeout(long curTime) {
        return true;
    }

    @Override
    public void reopen(long curTime) throws Exception {

    }


    public static void main(String[] args) throws Exception {

        String fpath = "F:\\shajf_dev\\antell\\3.4\\GBWScanner\\target\\fofa.json";
        GBWFoFaAPISourceConfig config = GsonUtils.loadConfigFromJsonFile(fpath,GBWFoFaAPISourceConfig.class);

        GBWFoFaAPISource apiSource = new GBWFoFaAPISource(config);

        apiSource.open();

        int count = apiSource.read(null);

        System.out.println(count);
        apiSource.close();
    }

}
