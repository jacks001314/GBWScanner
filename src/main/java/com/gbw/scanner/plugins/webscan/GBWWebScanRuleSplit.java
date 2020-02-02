package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.Host;

public class GBWWebScanRuleSplit {

    private final Host host;
    private final GBWWebScan webScan;
    private final  GBWWebScanRuleConfig ruleConfig;

    private int start;
    private int end;
    private int pos;

    public GBWWebScanRuleSplit(GBWWebScan webScan, Host host, int start, int end){

        this.host = host;
        this.webScan = webScan;

        this.ruleConfig = webScan.getScanRuleConfig();
        this.start = start;
        this.end = end;

        this.pos = start;
    }

    public String toString(){

        return String.format("start:%d,end:%d,pos:%d",start,end,pos);
    }

    public Host getHost() {
        return host;
    }

    public boolean hasNext(){

        return pos<=end;
    }

    public GBWWebScanRule next(){

        if(pos>end)
            return null;

        GBWWebScanRule rule =ruleConfig.getRule(pos);
        pos+=1;
        return rule;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }


    public GBWWebScan getWebScan() {
        return webScan;
    }
}
