package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.GBWScannerPlugin;
import com.gbw.scanner.Host;
import com.gbw.scanner.sink.SinkQueue;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GBWWebScanPlugin implements GBWScannerPlugin {

    private GBWWebScanConfig config;
    private GBWWebScanRuleSplitQueue splitQueue;
    private SinkQueue sinkQueue;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private GBWWebScan webScan;

    public GBWWebScanPlugin(GBWWebScanConfig config,SinkQueue sinkQueue) throws IOException {

        this.config = config;
        this.splitQueue = new GBWWebScanRuleSplitQueue();
        this.sinkQueue = sinkQueue;
        this.webScan = new GBWWebScanBase(config);
    }

    @Override
    public void start() {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(config.getThreads());

        for(int i = 0; i < config.getThreads(); ++i) {
            try {
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new GBWWebScanThread(splitQueue,sinkQueue), 0, 1, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean accept(Host host) {

        List<String> types = host.getTypes();
        if(types == null ||types.isEmpty())
            return false;

        return types.contains("webScan");
    }

    private void doWebScanRuleSplit(Host host){

        GBWWebScanRuleConfig ruleConfig = webScan.getScanRuleConfig();
        int n = config.getThreads();
        int dn = ruleConfig.getRulesNumber();
        int dv = dn/n;

        if(dv<=n||dv==dn){
            splitQueue.put(new GBWWebScanRuleSplit(webScan,host,0,dn-1));
        }else{

            GBWWebScanRuleSplit split =null;
            int dd = 0;

            while(dd<dn){

                if(dd+dv>=dn){

                    split.setEnd(dn-1);
                    break;
                }

                split = new GBWWebScanRuleSplit(webScan,host,dd,dd+dv-1);
                dd+=dv;

                splitQueue.put(split);
            }
        }
    }

    @Override
    public void process(Host host) {

        List<String> detectTypes = host.getTypes();

        if(detectTypes == null ||detectTypes.isEmpty())
            return;

        doWebScanRuleSplit(host);
    }

}
