package com.gbw.scanner.sink;

import com.gbw.scanner.GBWScannerConfig;

import com.gbw.scanner.sink.es.ESSink;
import com.gbw.scanner.sink.file.FileSink;

public class SinkFactory {

    private SinkFactory(){

    }

    public static Sink create(GBWScannerConfig config,SinkQueue sinkQueue){

        String sinkType = config.getSinkType();
        Sink sink = null;

        if(sinkType.equalsIgnoreCase(GBWScannerConfig.esSink)){

            sink = new ESSink(config.getEsSinkConfig(),config.getGeoIPConfig(),sinkQueue);
        }else if(sinkType.equalsIgnoreCase(GBWScannerConfig.fileSink)){

            sink = new FileSink(config.getFileSinkConfig(),sinkQueue);
        }

        return sink;
    }




}
