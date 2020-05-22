package com.gbw.scanner.sink.es;

import com.gbw.scanner.sink.SinkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class ESSinkQueue implements SinkQueue {

    private final static Logger log = LoggerFactory.getLogger(ESSinkQueue.class);

    private LinkedBlockingQueue<ESIndexable> queue;

    public ESSinkQueue(ESConfigItem esConfigItem) {

        int qnum = esConfigItem.getQueueLimits();

        if(qnum>0)
            queue = new LinkedBlockingQueue<ESIndexable>(qnum);
        else
            queue = new LinkedBlockingQueue<ESIndexable>();

    }

    public ESSinkQueue(){

        queue = new LinkedBlockingQueue<ESIndexable>();
    }

    @Override
    public void put(ESIndexable entry) {

        try {
            queue.put(entry);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void put(List<ESIndexable> arr) {

        try {
            queue.addAll(arr);
        }catch (Exception e){

        }
    }

    @Override
    public ESIndexable take() {

        if (queue.isEmpty())
            return null;

        ESIndexable entry = null;

        try {
            entry = queue.take();
        } catch (InterruptedException e) {

            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }

        return entry;
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public long getCount() {

        return queue.size();
    }


}
