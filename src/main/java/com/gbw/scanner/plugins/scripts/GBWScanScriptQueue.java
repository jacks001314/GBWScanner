package com.gbw.scanner.plugins.scripts;

import com.gbw.scanner.Host;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class GBWScanScriptQueue {


    private LinkedBlockingQueue<Host> queue;

    public GBWScanScriptQueue() {
        queue = new LinkedBlockingQueue<Host>();
    }

    public synchronized void put(Host host) {

        try {
            queue.put(host);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void put(List<Host> arr) {

        try {
            queue.addAll(arr);
        }catch (Exception e){

        }
    }

    public synchronized Host take() {

        if (queue.isEmpty())
            return null;

        Host host = null;

        try {
            host = queue.take();
        } catch (InterruptedException e) {

            e.printStackTrace();
            return null;
        }

        return host;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized long getCount() {

        return queue.size();
    }


}
