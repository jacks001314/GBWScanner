package com.gbw.scanner.sink.es;

import com.gbw.scanner.geoip.GeoIPConfigItem;
import com.gbw.scanner.sink.Sink;
import com.gbw.scanner.sink.SinkException;
import com.gbw.scanner.sink.SinkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ESSink implements Sink {

    private final static Logger logger = LoggerFactory.getLogger(ESSink.class);
    private SinkQueue sinkQueue;

    private ESConfigItem configItem;
    private GeoIPConfigItem geoIPConfigItem;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public ESSink(ESConfigItem configItem,GeoIPConfigItem geoIPConfigItem, SinkQueue sinkQueue) {

        this.configItem = configItem;
        this.geoIPConfigItem = geoIPConfigItem;
        this.sinkQueue = sinkQueue;

    }

    @Override
    public void start() throws SinkException {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(configItem.getThreadNum());

        for (int i = 0; i < configItem.getThreadNum(); i++) {
            try {
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new ESSinkRunnable(), 500, 500, TimeUnit.MILLISECONDS);
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new SinkException(e.getMessage());
            }
        }
    }

    @Override
    public void stop() {


    }

    private class ESSinkRunnable implements Runnable {

        private ESClient esClient;

        public ESSinkRunnable() throws IOException {

            this.esClient = ESClientFactory.create(configItem,geoIPConfigItem);
        }

        @Override
        public void run() {

            while (true) {

                try {

                    ESIndexable entry = (ESIndexable) sinkQueue.take();

                    if (entry == null) {
                        break;
                    }
                    esClient.index(entry);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
