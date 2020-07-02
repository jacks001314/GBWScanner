package com.gbw.scanner.source;

public class GBWHostSourceThread implements Runnable{

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

            if(sourcePool.isFull()){
                sleep();
                continue;
            }

            GBWHostSource source = sourcePool.getHostSource(curSource);
            if (source == null) {

                break;
            }
            int c = source.read(sourcePool);
            if(isEmpty(source,c)){
                processReadEnd(source);
                curSource = null;
            }else {
                curSource = source;
            }
        }
    }
}
