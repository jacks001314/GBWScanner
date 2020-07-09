package com.gbw.scanner.source;

import com.gbw.scanner.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GBWHostSourcePoolBasic implements GBWHostSourcePool {

    private Logger log = LoggerFactory.getLogger(GBWHostSourcePoolBasic.class);
    private final LinkedBlockingQueue<Host> hosts = new LinkedBlockingQueue();

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private LinkedBlockingQueue<GBWHostSource> sources;
    private GBWHostSourcePoolConfig config;

    public GBWHostSourcePoolBasic(GBWHostSourcePoolConfig config) throws Exception {

        this.config = config;
        this.sources = new LinkedBlockingQueue<>();

        loadSource();
    }

    private void loadSource() throws Exception {

        GBWHostSource hostSource = null;

        for (GBWHostSourcePoolConfigEntry entry : config.getSourceConfigs()) {

            if (entry.isOn()) {
                hostSource = GBWHostSourceFactory.create(entry);
                if (hostSource != null)
                    addSource(hostSource);
            }
        }
    }

    @Override
    public boolean isFull() {
        return hosts.size() >= config.getQueueLimits();
    }

    @Override
    public boolean isEmpty() {
        return hosts.isEmpty();
    }

    @Override
    public Host get() {

        if (hosts.isEmpty()) {
            return null;
        } else {
            try {
                return (Host) hosts.take();
            } catch (InterruptedException var2) {
                return null;
            }
        }
    }

    @Override
    public void put(Host host) {
        try {
            hosts.put(host);
        } catch (InterruptedException var3) {
        }

    }

    @Override
    public void start() {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new GBWHostSourceThread(this), 0L, (long) config.getSleepTime(), TimeUnit.MILLISECONDS);

    }


    @Override
    public void stop() {

    }

    @Override
    public  void addSource(GBWHostSource source) {

        try {
            sources.put(source);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Add a source,the source number:" + sources.size());
    }

    @Override
    public  void removeSource(GBWHostSource source) {

        sources.remove(source);
        source.close();
        log.info("Remove a source,the source number:" + sources.size());
    }

    public  GBWHostSource getHostSource(GBWHostSource curSource) {

        if (sources.isEmpty()) {
            /*no sources*/
            return null;
        }

        for (GBWHostSource hostSource : sources) {

            if (hostSource != curSource)
                return hostSource;
        }

        return curSource;
    }


}
