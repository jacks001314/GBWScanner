package com.gbw.scanner;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForcePlugin;
import com.gbw.scanner.plugins.detect.GBWDetectPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.webscan.GBWWebScanPlugin;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.source.GBWESHostSource;
import com.gbw.scanner.source.GBWFileLineSource;
import com.gbw.scanner.source.GBWHostSource;
import com.gbw.scanner.source.GBWShodanSource;
import com.xmap.api.SourceException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GBWScannerThread {

    private final SinkQueue sinkQueue;
    private final List<GBWScannerPlugin> plugins;
    private final GBWScannerConfig config;
    private GBWHostSource hostSource;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public GBWScannerThread(GBWScannerConfig config, SinkQueue sinkQueue) throws Exception {

        this.sinkQueue = sinkQueue;
        this.plugins = new ArrayList<>();
        this.config = config;

        if (config.isOnBruteForce()) {
            plugins.add(new GBWBruteForcePlugin(config.getBruteForceConfig(), sinkQueue));
        }

        if(config.isOnDetect()){

            plugins.add(new GBWDetectPlugin(config.getDetectConfig(),sinkQueue));
        }

        if(config.isOnWebDetect()){

            plugins.add(new GBWWebScanPlugin(config.getWebScanConfig(),sinkQueue));
        }
        if(config.isOnScanScript()){
            plugins.add(new GBWScanScriptPlugin(sinkQueue,config.getScanScriptConfig()));
        }

        String sType = config.getStype();
        if (sType.equals(GBWScannerConfig.SOURCETYPEES)) {
            this.hostSource = new GBWESHostSource(config.getsESConfig());
        } else if (sType.equals(GBWScannerConfig.SOURCETYPEFILELINE)) {
            this.hostSource = new GBWFileLineSource(config.getsFileLineConfig());
        } else if(sType.equals(GBWScannerConfig.SOURCETYPESHODAN)) {
            this.hostSource = new GBWShodanSource(config.getsShodanConfig());
        }else{
            throw new IllegalArgumentException("Unkown host source type:" + sType);
        }

    }


    public void start() throws SourceException {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new GBWScannerThreadRunnable(), 0, 1, TimeUnit.MINUTES);

        hostSource.start();
        plugins.forEach(plugin -> plugin.start());

    }

    public void stop() {

        hostSource.stop();
        plugins.forEach(plugin->plugin.stop());

    }

    private class GBWScannerThreadRunnable implements Runnable {


        private void process(Host host) {

            for (GBWScannerPlugin plugin : plugins) {

                if (plugin.accept(host)) {
                    plugin.process(host);
                }
            }
        }

        @Override
        public void run() {

            while (true) {

                try {
                    if (hostSource.isEmpty())
                        break;

                    Host host = hostSource.get();
                    if (host == null)
                        break;

                    process(host);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        }

    }
}
