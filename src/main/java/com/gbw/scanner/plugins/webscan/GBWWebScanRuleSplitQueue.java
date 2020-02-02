package com.gbw.scanner.plugins.webscan;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class GBWWebScanRuleSplitQueue {


    private LinkedBlockingQueue<GBWWebScanRuleSplit> queue;

    public GBWWebScanRuleSplitQueue() {
        queue = new LinkedBlockingQueue<GBWWebScanRuleSplit>();
    }

    public synchronized void put(GBWWebScanRuleSplit ruleSplit) {

        try {
            queue.put(ruleSplit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void put(List<GBWWebScanRuleSplit> arr) {

        try {
            queue.addAll(arr);
        }catch (Exception e){

        }
    }

    public synchronized GBWWebScanRuleSplit take() {

        if (queue.isEmpty())
            return null;

        GBWWebScanRuleSplit ruleSplit = null;

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
