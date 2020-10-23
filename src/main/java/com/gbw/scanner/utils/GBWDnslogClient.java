package com.gbw.scanner.utils;

import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

public class GBWDnslogClient {

    private CloseableHttpClient httpClient;
    private final String subDomain;
    private final String host;
    private final int port;

    public GBWDnslogClient(String domain,String host, int port) throws Exception {

        this.subDomain = String.format("%s.%s", RandomUtils.makeRandomText(5),domain);
        this.host = host;
        this.port = port;

        httpClient = GBWHttpClientBuilder.make("http",80);
    }

    public void close(){

        try {
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String search(){

        try {

            HttpGet httpGet = new GBWHttpGetRequestBuilder("http",host,port,"/search?id="+subDomain)
                    .addHead("User-Agent","dnslog-client")
                    .setTimeout((int)10000,(int)10000)
                    .build();

            GBWHttpResponse response = HttpUtils.send(httpClient,httpGet,true);


            String res = response.getContent();

            if(!TextUtils.isEmpty(res)){

                SearchResult searchResult = GsonUtils.loadConfigFromJson(res,SearchResult.class);

                if(searchResult.getStatus() == 0){

                    return searchResult.getResult();
                }

            }

        }catch (Exception e){

        }

        return null;
    }

    public void delete(){

        try {

            HttpGet httpGet = new GBWHttpGetRequestBuilder("http",host,port,"/remove?id="+subDomain)
                    .addHead("User-Agent","dnslog-client")
                    .setTimeout((int)10000,(int)10000)
                    .build();

            GBWHttpResponse response = HttpUtils.send(httpClient,httpGet,true);

        }catch (Exception e){

        }

    }

    public String getSubDomain() {
        return subDomain;
    }

    private class SearchResult{

        private String op;
        private int status;
        private String msg;
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getOp() {
            return op;
        }

        public void setOp(String op) {
            this.op = op;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }



}
