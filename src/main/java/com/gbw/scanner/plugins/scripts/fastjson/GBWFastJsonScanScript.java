package com.gbw.scanner.plugins.scripts.fastjson;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.GBWDnslogClient;
import com.gbw.scanner.utils.GsonUtils;
import com.xmap.api.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GBWFastJsonScanScript implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWFastJsonScanScript.class);

    private GBWFastJsonScanConfig config;
    private GBWFastJsonURLConfig urlConfig;

    private List<GBWFastJsonCheckPayload> checkPayloads;

    public GBWFastJsonScanScript(GBWFastJsonScanConfig config) throws Exception {

        this.config = config;
        this.urlConfig = GsonUtils.loadConfigFromJsonFile(config.getUrlPath(),GBWFastJsonURLConfig.class);
        this.checkPayloads = new ArrayList<>();
        parseCheckPayload();
    }

    private void parseCheckPayload() throws IOException {

        for(GBWFastJsonTplEntry entry:config.getEntries()){

            checkPayloads.add(new GBWFastJsonCheckPayload(entry,
                    config.getDnslogDomain(),config.getTplDir(),config.getProto()));

        }

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

            GBWDnslogClient dnslogClient = new GBWDnslogClient(config.getDnslogDomain(),config.getDnslogHost(),config.getDnslogPort());

            for(GBWFastJsonCheckPayload checkPayload:checkPayloads){

                for(GBWFastJsonURL checkURL:urlConfig.getUrls()){

                    GBWFastJsonHttpClient.send(host,checkURL,checkPayload.getPayload());
                }

            }
            Thread.sleep(config.getScanSleepTime());

            String response = dnslogClient.search();


            if(!TextUtils.isEmpty(response)){

                GBWFastJsonScanResult result = new GBWFastJsonScanResult(config,host);
                result.setDnsDomain(dnslogClient.getSubDomain());

                sinkQueue.put(result);

                log.info(result.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
