package com.gbw.scanner.plugins.webscan;

import java.util.List;

public class GBWWebScanRuleConfig {

    private List<GBWWebScanRule> rules;

    public GBWWebScanRule getRule(int pos){

        if(pos<0||rules==null||pos>=rules.size()||rules.size()==0)
            return null;

        return rules.get(pos);
    }
    public int getRulesNumber(){

        return rules == null?0:rules.size();
    }

    public List<GBWWebScanRule> getRules() {
        return rules;
    }

    public void setRules(List<GBWWebScanRule> rules) {
        this.rules = rules;
    }

}
