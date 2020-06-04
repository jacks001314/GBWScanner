package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.utils.FileUtils;
import com.gbw.scanner.utils.GsonUtils;
import com.gbw.scanner.utils.RuleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GBWWebScanRuleConfigFactory {

    private static final Logger log = LoggerFactory.getLogger(GBWWebScanRuleConfigFactory.class);

    private GBWWebScanRuleConfigFactory(){
    }

    private static void merge(GBWWebScanRuleConfig dstConfig, GBWWebScanRuleConfig config){

        List<GBWWebScanRule> dstRules = dstConfig.getRules();
        List<GBWWebScanRule> rules = config.getRules();

        if(rules==null||rules.size() == 0)
            return;

        rules.forEach(rule->{
            if(rule.isEnable())
                dstRules.add(rule);
        });


    }

    public static GBWWebScanRuleConfig create(String ruleRootDir){

        GBWWebScanRuleConfig webScanRuleConfig = new GBWWebScanRuleConfig();
        webScanRuleConfig.setRules(new ArrayList<>());

        List<String> dirs = FileUtils.dirs(ruleRootDir);
        if(dirs == null ||dirs.size()==0){

            log.warn(String.format("WEBScan Rule file dir:%s,no any rule files!",ruleRootDir));
            return null;
        }

        for(String dir:dirs){

            String fpath = RuleUtils.getRulePath(ruleRootDir,dir);
            if(FileUtils.isExisted(fpath)){

                try {
                    GBWWebScanRuleConfig config = GsonUtils.loadConfigFromJsonFile(fpath,GBWWebScanRuleConfig.class);
                    log.info(String.format("Load webscan rule file:%s is ok,the rule number:%d",fpath,config.getRules().size()));
                    merge(webScanRuleConfig,config);

                } catch (IOException e) {
                    log.error(String.format("Load webscan rule file:%s failed!",fpath));
                }
            }
        }

        log.info(String.format("Load webscan rule total number:%d",webScanRuleConfig.getRules().size()));

        return webScanRuleConfig;
    }
}
