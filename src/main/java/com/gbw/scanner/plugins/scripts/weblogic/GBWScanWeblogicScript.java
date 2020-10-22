package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.weblogic.vuls.*;
import com.gbw.scanner.sink.SinkQueue;

import java.util.ArrayList;
import java.util.List;

public class GBWScanWeblogicScript implements GBWScanScript {

    private GBWScanWeblogicConfig config;
    private List<GBWEchoVul> echoVuls;
    private List<GBWNoEchoVul> noEchoVuls;

    public GBWScanWeblogicScript(GBWScanWeblogicConfig config) throws Exception {

        this.config = config;

        this.echoVuls = new ArrayList<>();
        this.noEchoVuls = new ArrayList<>();

        resister();
    }

    private void resister() throws Exception {

        if(config.isIsonCVE_2020_2555()){
            echoVuls.add(new GBWCVE_2020_2555(config));
            noEchoVuls.add(new GBWCVE_2020_2555_NOECHO(config));
        }

        if(config.isIsonCVE_2020_2551()) {
            echoVuls.add(new GBWCVE_2020_2551(config));
            noEchoVuls.add(new GBWCVE_2020_2551_NOECHO(config));
        }

        if(config.isOnCVE_2018_2894())
            echoVuls.add(new GBWCVE_2018_2894(config));

        if(config.isOnCVE_2016_0638())
            echoVuls.add(new GBWCVE_2016_0638(config));

        if(config.isOnCVE_2018_3245())
            noEchoVuls.add(new GBWCVE_2018_3245(config));

        if(config.isOnCVE_2018_3191())
            noEchoVuls.add(new GBWCVE_2018_3191(config));

        if(config.isOnCVE_2018_2893())
            noEchoVuls.add(new GBWCVE_2018_2893(config));

        if(config.isOnCVE_2018_2628())
            noEchoVuls.add(new GBWCVE_2018_2628(config));

        if(config.isOnCVE_2017_3248())
            noEchoVuls.add(new GBWCVE_2017_3248(config));

    }

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        try {
            /*scan echo vuls*/

            for(GBWEchoVul echoVul:echoVuls){

                GBWVulCheckResult scanResult = echoVul.scan(host);
                if(scanResult!=null){
                    GBWScanWeblogicResult result = new GBWScanWeblogicResult(config,host,scanResult);
                    if(sinkQueue!=null){
                        sinkQueue.put(result);
                    }
                    return;
                }
            }

            /*scan no echo vuls*/
            for (GBWNoEchoVul noEchoVul:noEchoVuls){

                GBWVulCheckResult scanResult = noEchoVul.scan(host);
                if(scanResult!=null){

                    GBWScanWeblogicResult result = new GBWScanWeblogicResult(config,host,scanResult);
                    if(sinkQueue!=null){
                        sinkQueue.put(result);
                    }
                    break;
                }
            }
        }catch (Exception e){

        }
    }

    public static void main(String[] args) throws Exception {

        GBWScanWeblogicConfig config =  new GBWScanWeblogicConfig();
        config.setReadTimeout(10000);
        config.setConTimeout(10000);
        config.setScanSleepTime(3000);
        config.setCmd("ls /tmp");
        config.setEchoShell("t3IIOP");
        config.setDefaultVersion("12130");
        config.setVersions(new String[]{"12130","12214"});
        config.setPayloadScriptDir("D:\\shajf_dev\\GBWScanner\\data");

        config.setDnslogDomain("dnslog.gbw3bao.com");
        config.setDnslogHost("47.93.48.162");
        config.setDnslogPort(80);
        //config.setIsonCVE_2020_2551(true);
        config.setOnCVE_2016_0638(true);
        //config.setIsonCVE_2020_2551(true);
        GBWScanWeblogicScript scanWeblogicScript = new GBWScanWeblogicScript(config);

        String ip = "192.168.1.160";
        int port = 7001;
        Host host = new Host(ip,ip,port,null,"t3");


        scanWeblogicScript.scan(host,null);
    }

}
