package com.gbw.scanner.plugins.detect;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class GBWDetectRuleSplitQueue {


    private LinkedBlockingQueue<GBWDetectRuleSplit> queue;

    public GBWDetectRuleSplitQueue() {
        queue = new LinkedBlockingQueue<GBWDetectRuleSplit>();
    }

    public synchronized void put(GBWDetectRuleSplit ruleSplit) {

        try {
            queue.put(ruleSplit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void put(List<GBWDetectRuleSplit> arr) {

        try {
            queue.addAll(arr);
        }catch (Exception e){

        }
    }

    public synchronized GBWDetectRuleSplit take() {

        if (queue.isEmpty())
            return null;

        GBWDetectRuleSplit ruleSplit = null;

        try {
            ruleSplit = queue.take();
        } catch (InterruptedException e) {

            e.printStackTrace();
            return null;
        }

        return ruleSplit;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized long getCount() {

        return queue.size();
    }


}
