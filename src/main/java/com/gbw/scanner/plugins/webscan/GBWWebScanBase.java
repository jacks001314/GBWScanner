package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.Host;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.GBWOPUtils;
import com.gbw.scanner.utils.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GBWWebScanBase implements GBWWebScan{

    private static final Logger log = LoggerFactory.getLogger(GBWWebScanBase.class);
    private final GBWWebScanConfig scanConfig;
    private final GBWWebScanRuleConfig scanRuleConfig;

    public GBWWebScanBase(GBWWebScanConfig scanConfig) throws IOException {

        this.scanConfig = scanConfig;
        if(scanConfig != null)
            this.scanRuleConfig = GsonUtils.loadConfigFromJsonFile(scanConfig.getRuleCPath(),GBWWebScanRuleConfig.class);
        else
            this.scanRuleConfig = null;
    }

    private boolean isMatch(HttpResponse response,GBWWebScanRule scanRule,GBWWebScanRuleData ruleData){

        byte[] bTarget = null;
        String sTarget = null;
        boolean isByte = ruleData.getTarget().equals("byte");

        if(isByte){
            bTarget = GBWWebVariables.getBody(response);
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

    private GBWWebScanResult detect(Host host,HttpResponse response,GBWWebScanRule scanRule){


        List<GBWWebScanRuleData> ruleDatas = scanRule.getMatches();
        boolean match = false;

        for(GBWWebScanRuleData ruleData:ruleDatas){

            match = isMatch(response,scanRule,ruleData);

            if(scanRule.isAnd()){
                if(!match)
                    return null;
            }else{
                if(match)
                    return new GBWWebScanResult(host,response,scanRule);
            }

        }

        return scanRule.isAnd()?new GBWWebScanResult(host,response,scanRule):null;
    }

    @Override
    public void scan(Host host, GBWWebScanRule scanRule, SinkQueue sinkQueue) {

        if(!scanRule.isEnable())
            return;

        List<HttpRequest> httpRequests = GBWHttpRequestBuilder.build(host,scanRule,scanConfig == null?5000:scanConfig.getTimeout());

        HttpClient client = HttpClient.newHttpClient();

        for(HttpRequest request:httpRequests){

            HttpResponse response = null;

            try {
                if(scanRule.isBin()){
                    response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                }else{
                    response = client.send(request,HttpResponse.BodyHandlers.ofString());
                }
            }catch (Exception e){
                response = null;
                // e.printStackTrace();
                // log.error(e.getMessage());
            }

            if(response!=null){

                GBWWebScanResult result = detect(host,response,scanRule);
                if(result!=null){

                    sinkQueue.put(result);
                }
            }
        }
    }


    @Override
    public GBWWebScanRuleConfig getScanRuleConfig() {
        return scanRuleConfig;
    }


}

