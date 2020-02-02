package com.gbw.scanner.plugins.detect;

import com.gbw.scanner.Host;

public class GBWDetectRuleSplit {

    private final Host host;
    private final GBWDetect detect;
    private final  GBWDetectRuleConfig ruleConfig;

    private int start;
    private int end;
    private int pos;

    public GBWDetectRuleSplit(GBWDetect detect, Host host, int start, int end){

        this.host = host;
        this.detect = detect;

        this.ruleConfig = detect.getRuleConfig();
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

    public GBWDetectRule next(){

        if(pos>end)
            return null;

        GBWDetectRule rule =ruleConfig.getRule(pos);
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

    public GBWDetect getDetect() {
        return detect;
    }
}
