package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.rule.GBWRuleMatch;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.GsonUtils;
import com.gbw.scanner.utils.HttpUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GBWWebScanBase implements GBWWebScan{

    private static final Logger log = LoggerFactory.getLogger(GBWWebScanBase.class);
    private final GBWWebScanConfig scanConfig;
    private final GBWWebScanRuleConfig scanRuleConfig;

    public GBWWebScanBase(GBWWebScanConfig scanConfig) throws IOException {

        this.scanConfig = scanConfig;
        if(scanConfig != null)
            this.scanRuleConfig = GBWWebScanRuleConfigFactory.create(scanConfig.getRuleDir());
        else
            this.scanRuleConfig = null;
    }


    private GBWWebScanResult detect(Host host,GBWHttpResponse response,GBWWebScanRule scanRule){

        boolean match = GBWRuleMatch.isMatch(response,scanRule);
        return match?new GBWWebScanResult(host,scanRule):null;

    }


    @Override
    public void scan(Host host, GBWWebScanRule scanRule, SinkQueue sinkQueue) {

        if(!scanRule.isEnable())
            return;

        HttpUriRequest httpRequest = null;
        try {
            httpRequest = GBWHttpRequestBuilder.build(host,scanConfig,scanRule);
        } catch (IOException e) {
           // e.printStackTrace();
            return;
        }

        CloseableHttpClient client = null;
        try {
            client = GBWHttpClientBuilder.make(host.getProto(),host.getPort());
        } catch (Exception e) {
           // e.printStackTrace();
            return;
        }

        GBWHttpResponse response = HttpUtils.sendWithHeaders(client,httpRequest,scanRule.isResBodyText());
        GBWWebScanResult result = detect(host,response,scanRule);

        if(result!=null){

            if(sinkQueue!=null){
                sinkQueue.put(result);
                String res = String.format("Find OK: webscan address:%s:%d for uri:%s,ruleID:%d",
                            host.getServer(),host.getPort(),httpRequest.getURI().toString(),scanRule.getId());
                log.warn(res);
            }else{
                String res = String.format("Find OK: webscan address:%s:%d for uri:%s,ruleID:%d",
                            host.getServer(),host.getPort(),httpRequest.getURI().toString(),scanRule.getId());

                System.out.println(res);
            }
        }

        try {
            client.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }


    @Override
    public GBWWebScanRuleConfig getScanRuleConfig() {
        return scanRuleConfig;
    }


}

