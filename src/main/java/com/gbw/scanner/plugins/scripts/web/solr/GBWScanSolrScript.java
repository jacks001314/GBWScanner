package com.gbw.scanner.plugins.scripts.web.solr;

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
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GBWScanSolrScript implements GBWScanScript {

    protected final GBWScanSolrScriptConfig config;

    public GBWScanSolrScript(GBWScanSolrScriptConfig config) {
        this.config = config;
    }

    public abstract void  processResult(SinkQueue sinkQueue, Host host, SolrCoreAdmin solrCoreAdmin, String result, String core);
    public abstract String runPoc(CloseableHttpClient httpClient, Host host, String core);

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    private static  final boolean isJson(String content){

        return !TextUtils.isEmpty(content)&&content.startsWith("{");
    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        String result;
        HttpUriRequest httpRequest;
        GBWHttpResponse httpResponse;
        String content;
        Gson gson = new Gson();

        CloseableHttpClient httpClient = null;

        /**get solr admin cores info*/
        httpRequest = SolrHttpRequestBuilder.makeSolrCoreAdminRequest(host,config);
        try {
            httpClient = GBWHttpClientBuilder.make(host.getProto(),host.getPort());
            httpResponse = HttpUtils.send(httpClient,httpRequest,true);

            if(httpResponse.getStatus()==200){

                content = httpResponse.getContent();

                if(isJson(content)){
                    /*parseu json content**/
                    SolrCoreAdmin solrCoreAdmin = gson.fromJson(content,SolrCoreAdmin.class);
                    /*get all core names*/
                    List<String> cores = solrCoreAdmin.getCoreNames();
                    if(cores.isEmpty()){
                        cores = config.getDefaultCores();
                    }
                    /*try to detect for all cores until find a bug */
                    for(String core:cores){
                        result = runPoc(httpClient,host,core);
                        if(!TextUtils.isEmpty(result)){
                            processResult(sinkQueue,host,solrCoreAdmin,result,core);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(String.format("scan solr host:%s:%d,error",host.getHost(),host.getPort()));
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


    protected void runMain(String[] args) throws Exception{

        Options opts = new Options();
        opts.addOption("cmd", true, "will run cmd");
        opts.addOption("resultLenMax", true, "run cmd result max length");
        opts.addOption("enCode",false,"encode url for running cmd");
        List<String> defaultCores = new ArrayList<>();
        defaultCores.add("collection1");
        defaultCores.add("core");
        defaultCores.add("core1");
        defaultCores.add("test");

        GBWScanScriptTool tool = new GBWScanScriptTool(args,this,opts,8983);

        CommandLine cli = tool.getCliParser();

        String cmd = "id";
        boolean isEncode = false;
        int resultLenMax = 128;

        if(cli.hasOption("cmd")){
            cmd = cli.getOptionValue("cmd");
        }

        if(cli.hasOption("enCode"))
            isEncode = true;

        if(cli.hasOption("resultLenMax"))
            resultLenMax = Integer.parseInt(cli.getOptionValue("resultLenMax"));

        config.setCmd(cmd);
        config.setDefaultCores(defaultCores);
        config.setEncode(isEncode);
        config.setResultLenMax(resultLenMax);

        tool.start();
    }

}
