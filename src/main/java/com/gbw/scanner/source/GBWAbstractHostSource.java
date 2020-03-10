package com.gbw.scanner.source;

import com.gbw.scanner.Host;
import com.xmap.api.SourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class GBWAbstractHostSource implements GBWHostSource {

    private Logger log = LoggerFactory.getLogger(GBWAbstractHostSource.class);
    protected final LinkedBlockingQueue<Host> sourceEntries = new LinkedBlockingQueue();
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private GBWHostSourceConfig config;

    public GBWAbstractHostSource(GBWHostSourceConfig config) {
        this.config = config;
    }

    public void start() throws SourceException {
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        this.scheduledThreadPoolExecutor.scheduleAtFixedRate(new SourceReadRunnable(), 0L, (long)this.config.getSleepTime(), TimeUnit.MILLISECONDS);
        this.preRead();
    }

    public void stop() {
        this.readEnd();
    }

    public abstract void preRead();

    public abstract int read();

    public abstract void readEnd();

    public boolean isEmpty() {
        return this.sourceEntries.isEmpty();
    }

    public Host get() {
        if (this.sourceEntries.isEmpty()) {
            return null;
        } else {
            try {
                return (Host)this.sourceEntries.take();
            } catch (InterruptedException var2) {
                return null;
            }
        }
    }

    public void put(Host host) {
        try {
            this.sourceEntries.put(host);
        } catch (InterruptedException var3) {
        }

    }

    private class SourceReadRunnable implements Runnable {
        private SourceReadRunnable() {
        }

        public void run() {
            while(true) {
                if (sourceEntries.size() >= config.getQueueLimits()) {
                    log.warn(String.format("Source Queue is exceeds max limits:%d,will sleep----------------------", config.getQueueLimits()));
                } else {
                    int c = read();
                    if (c != 0) {
                        log.info(String.format("Current queue etries:%d,read:%d",sourceEntries.size(),c));
                        continue;
                    }
                }

                return;
            }
        }
    }
}
