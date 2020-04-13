package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.Host;
import com.gbw.scanner.sink.SinkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWWebScanThread implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(GBWWebScanThread.class);
    private final GBWWebScanRuleSplitQueue splitQueue;
    private final SinkQueue sinkQueue;

    public GBWWebScanThread(GBWWebScanRuleSplitQueue splitQueue, SinkQueue sinkQueue) {

        this.splitQueue = splitQueue;
        this.sinkQueue = sinkQueue;
    }

    @Override
    public void run() {

        while (true){

            if(splitQueue.isEmpty())
                break;

            GBWWebScanRuleSplit split = splitQueue.take();
            if(split!=null){

                GBWWebScan webScan = split.getWebScan();

                while(split.hasNext()){

                    GBWWebScanRule rule = split.next();

                    Host host = split.getHost();

                    log.info(String.format("Start WebScan:{host:%s,ip:%s,port:%d,ruleID:%d,ruleType:%s}",
                            host.getHost(),host.getIp(),host.getPort(),rule.getId(),rule.getType()));

                    try {

                        webScan.scan(host,rule,sinkQueue);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
        }
    }

}
