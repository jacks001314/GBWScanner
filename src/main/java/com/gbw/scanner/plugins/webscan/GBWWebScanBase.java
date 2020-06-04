package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.Host;
import com.gbw.scanner.http.GBWHttpClientBuilder;
import com.gbw.scanner.http.GBWHttpResponse;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.GBWOPUtils;
import com.gbw.scanner.utils.HttpUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

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

    private boolean isMatch(GBWHttpResponse response,GBWWebScanRule scanRule,GBWWebScanRuleData ruleData){

        byte[] bTarget = null;
        String sTarget = null;
        boolean isByte = ruleData.getTarget().equals("byte");

        if(isByte){
            bTarget = response.getBytes();
            if(bTarget == null||bTarget.length==0)
                return false;

        }else{
            sTarget = GBWWebVariables.getString(response,ruleData.getTarget());
            if(sTarget == null ||sTarget.length()==0)
                return false;
        }

        if(isByte){
            return GBWOPUtils.isMatch(bTarget,ruleData.getOp(),ruleData.getValue());
        }else{
            return GBWOPUtils.isMatch(sTarget,ruleData.getOp(),ruleData.getValue());
        }
    }

    private GBWWebScanResult detect(Host host,GBWHttpResponse response,GBWWebScanRule scanRule){


        List<GBWWebScanRuleData> ruleDatas = scanRule.getMatches();
        boolean match = false;

        for(GBWWebScanRuleData ruleData:ruleDatas){

            match = isMatch(response,scanRule,ruleData);

            if(scanRule.isAnd()){
                if(!match)
                    return null;
            }else{
                if(match)
                    return new GBWWebScanResult(host,scanRule);
            }

        }

        return scanRule.isAnd()?new GBWWebScanResult(host,scanRule):null;
    }

    @Override
    public void scan(Host host, GBWWebScanRule scanRule, SinkQueue sinkQueue) {

        if(!scanRule.isEnable())
            return;

        List<HttpUriRequest> httpRequests = null;
        try {
            httpRequests = GBWHttpRequestBuilder.build(host,scanConfig,scanRule);
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

        for(HttpUriRequest request:httpRequests){

            GBWHttpResponse response = HttpUtils.sendWithHeaders(client,request,scanRule.isBin()?false:true);
            GBWWebScanResult result = detect(host,response,scanRule);
            if(result!=null){

                if(sinkQueue!=null){
                    sinkQueue.put(result);
                    String res = String.format("Find OK: webscan address:%s:%d for uri:%s,ruleID:%d",
                            host.getServer(),host.getPort(),request.getURI().toString(),scanRule.getId());
                    log.warn(res);
                }else{

                    String res = String.format("Find OK: webscan address:%s:%d for uri:%s,ruleID:%d",
                            host.getServer(),host.getPort(),request.getURI().toString(),scanRule.getId());

                    System.out.println(res);
                }
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

