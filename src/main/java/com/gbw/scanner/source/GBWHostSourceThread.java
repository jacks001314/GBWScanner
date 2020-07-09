package com.gbw.scanner.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWHostSourceThread implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(GBWHostSourceThread.class);
    private GBWHostSourcePool sourcePool;

    public GBWHostSourceThread(GBWHostSourcePool sourcePool) {

        this.sourcePool = sourcePool;
    }

    private boolean isEmpty(GBWHostSource source,int c){

        return  c == 0;
    }

    private void sleep(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }

    private void processReadEnd(GBWHostSource source){

        if(source.isRemove()){
            sourcePool.removeSource(source);
        }else{

            long curTime = System.currentTimeMillis();
            if(source.isTimeout(curTime)){
                try {
                    source.reopen(curTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {

        GBWHostSource curSource = null;

        while (true) {
            try {

                if(sourcePool.isFull()) {
                    log.info("The host pool is full,will sleep.....");
                    sleep();
                    continue;
                }

                GBWHostSource source = sourcePool.getHostSource(curSource);
                if (source == null) {
                    break;
                }

                int c = source.read(sourcePool);

                curSource = source;

                if(isEmpty(source,c)){
                    processReadEnd(source);
                    if(source.isRemove())
                        curSource = null;
                    //sleep();
                }

            }catch (Exception e){

                log.error("Read source failed:"+e.getMessage());
                sleep();
                break;
            }
        }
    }
}
