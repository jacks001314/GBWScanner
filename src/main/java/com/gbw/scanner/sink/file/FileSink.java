package com.gbw.scanner.sink.file;

import com.gbw.scanner.sink.Sink;
import com.gbw.scanner.sink.SinkException;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.sink.es.ESIndexable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FileSink implements Sink {

    private static final Logger logger = LoggerFactory.getLogger(FileSink.class);

    private final FileSinkConfig fileSinkConfig;
    private final SinkQueue sinkQueue;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public FileSink(FileSinkConfig fileSinkConfig,SinkQueue sinkQueue){

        this.fileSinkConfig = fileSinkConfig;
        this.sinkQueue = sinkQueue;
    }

    @Override
    public void start() throws SinkException {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(fileSinkConfig.getThreadNum());

        for (int i = 0; i < fileSinkConfig.getThreadNum(); i++) {
            try {
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new FileSinkRunnable(i+1),500, 500, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new SinkException(e.getMessage());
            }
        }
    }


    private class FileSinkRunnable implements Runnable{

        private FileSinkWriter fileSinkWriter;

        public FileSinkRunnable(int n) throws IOException {

            fileSinkWriter = FileSinkWriterFactory.create(fileSinkConfig,n);
            if(fileSinkWriter == null)
                throw new NullPointerException();
        }

        @Override
        public void run() {

            while (true) {

                try {

                    ESIndexable entry = (ESIndexable) sinkQueue.take();

                    if (entry == null) {
                        break;
                    }

                    fileSinkWriter.writeLine(entry);
                    fileSinkWriter.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void stop() {


    }

}
