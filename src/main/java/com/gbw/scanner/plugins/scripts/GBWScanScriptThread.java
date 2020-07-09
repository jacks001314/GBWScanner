package com.gbw.scanner.plugins.scripts;

import com.gbw.scanner.Host;
import com.gbw.scanner.sink.SinkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GBWScanScriptThread implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GBWScanScriptThread.class);

    private final SinkQueue sinkQueue;
    private final GBWScanScriptQueue scanScriptQueue;
    private final Map<String,GBWScanScript> scanScriptMap;

    public GBWScanScriptThread(SinkQueue sinkQueue, GBWScanScriptQueue scanScriptQueue, Map<String, GBWScanScript> scanScriptMap) {
        this.sinkQueue = sinkQueue;
        this.scanScriptQueue = scanScriptQueue;
        this.scanScriptMap = scanScriptMap;
    }

    @Override
    public void run() {


        while(true){

            try {
                Host host = scanScriptQueue.take();
                if(host==null){
                    break;
                }
                for(String scanType:host.getTypes()){
                    GBWScanScript scanScript = scanScriptMap.get(scanType);
                    if(scanScript!=null){
                        log.info(String.format("Start Scan script:%s for host:%s:%d",scanType,host.getIp(),host.getPort()));

                        if(scanScript.isAccept(host))
                            scanScript.scan(host,sinkQueue);
                    }
                }
            }catch (Exception e){

                break;
            }
        }
    }

}
