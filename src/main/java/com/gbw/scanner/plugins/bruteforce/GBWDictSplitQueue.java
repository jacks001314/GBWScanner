package com.gbw.scanner.plugins.bruteforce;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class GBWDictSplitQueue {


    private LinkedBlockingQueue<GBWDictSplit> queue;

    public GBWDictSplitQueue() {
        queue = new LinkedBlockingQueue<GBWDictSplit>();
    }

    public synchronized void put(GBWDictSplit entry) {

        try {
            queue.put(entry);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void put(List<GBWDictSplit> arr) {

        try {
            queue.addAll(arr);
        }catch (Exception e){

        }
    }

    public synchronized GBWDictSplit take() {

        if (queue.isEmpty())
            return null;

        GBWDictSplit entry = null;

        try {
            entry = queue.take();
        } catch (InterruptedException e) {

            e.printStackTrace();
            return null;
        }

        return entry;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized long getCount() {

        return queue.size();
    }


}
