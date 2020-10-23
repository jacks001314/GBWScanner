package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.weblogic.*;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWNoEchoPayload;
import com.gbw.scanner.utils.GBWDnslogClient;
import com.gbw.scanner.utils.Serializer;
import com.xmap.api.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class GBWAbstractNoEchoVul implements GBWNoEchoVul {

    private static final Logger log = LoggerFactory.getLogger(GBWAbstractNoEchoVul.class);

    protected GBWScanWeblogicConfig config;
    protected String type;
    protected String code;

    protected List<GBWNoEchoPayload> noEchoPayloads;


    public GBWAbstractNoEchoVul(GBWScanWeblogicConfig config,String type,String code){

        this.config = config;
        this.noEchoPayloads = new ArrayList<>();

        this.type = type;
        this.code = code;
    }

    public abstract String getDefaultProto();

    private void changeProto(Host host){

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

        for (GBWNoEchoPayload payload : noEchoPayloads) {

            log.info(String.format("For weblogic version:%s,check payload:%s",version,payload.getClass().getSimpleName()));

            GBWDnslogClient dc = null;

            try {
                dc = new GBWDnslogClient(config.getDnslogDomain(),config.getDnslogHost(),config.getDnslogPort());

                String domain = dc.getSubDomain();

                Host nhost = new Host(domain,domain,1099,null,"ldap");

                if(host.getProto().equals("iiop")){

                    GBWPayloadSend.sendIIOP(config,host,payload.getObject(nhost));
                }else{
                    GBWPayloadSend.sendT3(config,host,Serializer.serialize(payload.getObject(nhost)));
                }

                Thread.sleep(config.getScanSleepTime());

                String result = dc.search();

                if (!TextUtils.isEmpty(result)) {

                    log.warn(String.format("Find a weblogic vul,version:%s,check payload:%s,type:%s,code:%s,domain:%s,result:%s,url:%s//%s:%d",
                                version,
                                payload.getName(),
                                type,
                                code,
                                domain,
                                result,
                                host.getProto(),
                                host.getIp(),
                                host.getPort()));

                    System.out.println(String.format("Find a weblogic vul,version:%s,check payload:%s,type:%s,code:%s,domain:%s,result:%s,url:%s//%s:%d",
                                version,
                                payload.getName(),
                                type,
                                code,
                                domain,
                                result,
                                host.getProto(),
                                host.getIp(),
                                host.getPort()));

                    return new GBWVulCheckResult(version,code,type,domain,result,payload.getName());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {

                if(dc!=null)
                    dc.close();
            }
        }

        return null;
    }
}
