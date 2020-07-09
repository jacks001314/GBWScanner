package com.gbw.scanner.plugins.bruteforce;

import com.gbw.scanner.Host;
import com.gbw.scanner.sink.SinkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWBruteForceThread implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(GBWBruteForceThread.class);

    private final GBWDictSplitQueue splitQueue;
    private final SinkQueue sinkQueue;

    public GBWBruteForceThread(GBWDictSplitQueue splitQueue, SinkQueue sinkQueue){

        this.splitQueue = splitQueue;
        this.sinkQueue = sinkQueue;
    }

    @Override
    public void run() {

        while (true){

            try{
                if(splitQueue.isEmpty())
                    break;

                GBWDictSplit split = splitQueue.take();
                if(split!=null){
                    GBWBruteForce bruteForce = split.getBruteForce();
                    while(split.hasNext()){
                        GBWDictEntry entry = split.next();
                        Host host = split.getHost();

                        log.info(String.format("Start BruteForce:{proto:%s,host:%s,ip:%s,port:%d,user:%s,passwd:%s}",
                            host.getProto(),host.getHost(),host.getIp(),host.getPort(),entry.getUser(),entry.getPasswd()));

                        GBWBruteForceResult bruteForceResult = bruteForce.bruteForce(host,entry);

                        if(bruteForceResult!=null){
                            log.info(String.format("BruteForce ok:{proto:%s,host:%s,ip:%s,port:%d,user:%s,passwd:%s,cmd:%s,cmdResult:%s}",
                                host.getProto(),host.getHost(),host.getIp(),host.getPort(),entry.getUser(),entry.getPasswd(),
                                bruteForceResult.getCmd(),bruteForceResult.getCmdResult()));
                        if(sinkQueue!=null){
                            sinkQueue.put(bruteForceResult);
                        }else{

                            System.out.println(String.format("BruteForce ok:{proto:%s,host:%s,ip:%s,port:%d,user:%s,passwd:%s,cmd:%s,cmdResult:%s}",
                                    host.getProto(),host.getHost(),host.getIp(),host.getPort(),entry.getUser(),entry.getPasswd(),
                                    bruteForceResult.getCmd(),bruteForceResult.getCmdResult()));
                        }
                    }
                }
            }
        }catch (Exception e){
                break;
            }
        }
    }
}
