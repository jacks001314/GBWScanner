package com.gbw.scanner.plugins.detect;

import com.gbw.scanner.utils.FileUtils;
import com.gbw.scanner.utils.GsonUtils;
import com.gbw.scanner.utils.RuleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GBWDetectRuleConfigFactory {


    private static final Logger log = LoggerFactory.getLogger(GBWDetectRuleConfigFactory.class);

    private GBWDetectRuleConfigFactory(){
    }

    private static void merge(GBWDetectRuleConfig dstConfig,GBWDetectRuleConfig config){

        List<GBWDetectRule> dstRules = dstConfig.getRules();
        List<GBWDetectRule> rules = config.getRules();

        if(rules==null||rules.size() == 0)
            return;

        rules.forEach(rule->{
            if(rule.isEnable())
                dstRules.add(rule);
        });


    }

    public static GBWDetectRuleConfig create(String ruleRootDir){

        GBWDetectRuleConfig detectRuleConfig = new GBWDetectRuleConfig();
        detectRuleConfig.setRules(new ArrayList<>());

        List<String> dirs = FileUtils.dirs(ruleRootDir);
        if(dirs == null ||dirs.size()==0){

            log.warn(String.format("Detect Rule file dir:%s,no any rule files!",ruleRootDir));
            return null;
        }

        for(String dir:dirs){

            String fpath = RuleUtils.getRulePath(ruleRootDir,dir);
            if(FileUtils.hasContent(fpath)){

                try {
                    GBWDetectRuleConfig config = GsonUtils.loadConfigFromJsonFile(fpath,GBWDetectRuleConfig.class);
                    log.info(String.format("Load detect rule file:%s is ok,the rule number:%d",fpath,config.getRules().size()));
                    merge(detectRuleConfig,config);

                } catch (Exception e) {
                    log.error(String.format("Load detect rule file:%s failed!",fpath));
                }
            }
        }

        log.info(String.format("Load detect rule total number:%d",detectRuleConfig.getRules().size()));

        return detectRuleConfig;
    }
}
