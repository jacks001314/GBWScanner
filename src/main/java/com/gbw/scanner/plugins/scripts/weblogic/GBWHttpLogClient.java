package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpGetRequestBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.utils.GsonUtils;
import com.gbw.scanner.utils.HttpUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Objects;

public class GBWHttpLogClient {

    private  String id;
    private final HttpLogContent logContent;
    private final String logIP;
    private final int logPort;

    public GBWHttpLogClient(Host host, String logIP,int logPort,String code){

        id = TextUtils.getUUID();

        HttpLogEntry logEntry = new HttpLogEntry();
        logEntry.setCode(code);
        logEntry.setIp(host.getIp());
        logEntry.setPort(host.getPort());

        logContent = new HttpLogContent();
        logContent.setId(id);
        logContent.setContent(GsonUtils.toJson(logEntry,false));

        this.logIP = logIP;
        this.logPort = logPort;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String makeRequestCmd(String httpClientName){

        String json =GsonUtils.toJson(logContent,true);
        return String.format("%s http://%s:%d/store?content=%s",httpClientName,logIP,logPort,json);
    }

    public boolean hasResponse(){

        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        GBWHttpResponse httpResponse = null;

        boolean isOK = false;

        try {

            httpClient = GBWHttpClientBuilder.make("http",logPort);
            httpGet = new GBWHttpGetRequestBuilder("http",logIP,logPort,String.format("/search?id=%s",id))
                    .build();

            httpResponse = HttpUtils.send(httpClient,httpGet,true);

            if(httpResponse.getStatus() == 200){

                    isOK = true;
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

        return isOK;
    }


    public String getId() {
        return id;
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

    private class HttpLogContent {

        private String id;
        private String content;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    private class HttpLogEntry {

        private String ip;
        private int port;
        private String code;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HttpLogEntry logEntry = (HttpLogEntry) o;
            return port == logEntry.port &&
                    ip.equals(logEntry.ip) &&
                    code.equals(logEntry.code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ip, port, code);
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
