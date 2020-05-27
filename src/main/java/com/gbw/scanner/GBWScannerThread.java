package com.gbw.scanner;

import com.gbw.scanner.cmd.GBWCmdThread;
import com.gbw.scanner.plugins.bruteforce.GBWBruteForcePlugin;
import com.gbw.scanner.plugins.detect.GBWDetectPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.webscan.GBWWebScanPlugin;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.source.GBWHostSourceCmdHandle;
import com.gbw.scanner.source.GBWHostSourcePool;
import com.gbw.scanner.source.GBWHostSourcePoolBasic;
import com.xmap.api.SourceException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GBWScannerThread {

    private final SinkQueue sinkQueue;
    private final List<GBWScannerPlugin> plugins;
    private final GBWScannerConfig config;
    private GBWHostSourcePool sourcePool;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private GBWCmdThread cmdThread;

    public GBWScannerThread(GBWScannerConfig config, SinkQueue sinkQueue, GBWCmdThread cmdThread) throws Exception {

        this.sinkQueue = sinkQueue;
        this.plugins = new ArrayList<>();
        this.config = config;
        this.cmdThread = cmdThread;

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

        this.sourcePool = new GBWHostSourcePoolBasic(config.getHostSourcePoolConfig());

        cmdThread.registerHandle(new GBWHostSourceCmdHandle(sourcePool));

    }


    public void start() throws SourceException {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new GBWScannerThreadRunnable(), 0, 1, TimeUnit.MINUTES);

        sourcePool.start();
        plugins.forEach(plugin -> plugin.start());

    }

    public void stop() {

        sourcePool.stop();
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
                    if (sourcePool.isEmpty())
                        break;

                    Host host = sourcePool.get();
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
