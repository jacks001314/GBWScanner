package com.gbw.scanner.plugins.detect;

import com.gbw.scanner.Host;
import com.gbw.scanner.sink.SinkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWDetectThread implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GBWDetectThread.class);

    private final GBWDetectRuleSplitQueue splitQueue;
    private final SinkQueue sinkQueue;

    public GBWDetectThread(GBWDetectRuleSplitQueue splitQueue, SinkQueue sinkQueue){

        this.splitQueue = splitQueue;
        this.sinkQueue = sinkQueue;
    }

    @Override
    public void run() {

        while (true){

            if(splitQueue.isEmpty())
                break;

            GBWDetectRuleSplit split = splitQueue.take();
            if(split!=null){

                GBWDetect detect = split.getDetect();

                while(split.hasNext()){

                    GBWDetectRule entry = split.next();

                    Host host = split.getHost();


                    log.info(String.format("Start Detect:{host:%s,ip:%s,port:%d,ruleID:%d,ruleType:%s}",
                            host.getHost(),host.getIp(),host.getPort(),entry.getId(),entry.getType()));


                    try {
                        GBWDetectResult detectResult = detect.detect(host,entry);
                        if(detectResult!=null){

                            log.info(String.format("Start Detect OK:{host:%s,ip:%s,port:%d,ruleID:%d,ruleType:%s}",
                                    host.getHost(),host.getIp(),host.getPort(),entry.getId(),entry.getType()));

                            if(sinkQueue!=null){
                                sinkQueue.put(detectResult);
                            }else{
                                System.out.println(String.format("Find OK:{host:%s,ip:%s,port:%d,ruleID:%d,ruleType:%s}",
                                        host.getHost(),host.getIp(),host.getPort(),entry.getId(),entry.getType()));
                            }
                        }
                    } catch (GBWDetectException e) {
                       // e.printStackTrace();

                    }


                }
            }
        }
    }

}
