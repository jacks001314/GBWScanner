package com.gbw.scanner.plugins.detect;

import java.util.List;

public class GBWDetectRuleConfig {

    private List<GBWDetectRule> rules;

    public List<GBWDetectRule> getRules() {
        return rules;
    }

    public void setRules(List<GBWDetectRule> rules) {
        this.rules = rules;
    }

    public int getRulesNumber(){

        return rules == null?0:rules.size();
    }

    public GBWDetectRule getRule(int pos){

        if(rules==null || rules.size()==0||pos<0||pos>=rules.size())
            return null;

        return rules.get(pos);
    }

}
