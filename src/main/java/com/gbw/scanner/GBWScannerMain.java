package com.gbw.scanner;

import com.gbw.scanner.sink.Sink;
import com.gbw.scanner.sink.SinkFactory;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.sink.es.ESSinkQueue;
import com.gbw.scanner.utils.GsonUtils;
import com.gbw.scanner.utils.KeepAliveThread;


public class GBWScannerMain {

    public static void main(String[] args) throws Exception{

        if(args.length <1){

            System.err.println("Usage: GBWScannerMain <configName>");
            System.exit(-1);
        }

        /*load config file */
        GBWScannerConfig config = GsonUtils.loadConfigFromJsonFile(args[0],GBWScannerConfig.class);


        /*Create sinkqueue */
        SinkQueue sinkQueue = new ESSinkQueue(config.getEsSinkConfig());

        /*create GBWScanner Thread*/
        GBWScannerThread scannerThread = new GBWScannerThread(config,sinkQueue);

        /*create sink */
        Sink sink = SinkFactory.create(config,sinkQueue);

        scannerThread.start();
        sink.start();

        /*keepalive main thread*/
        Runtime.getRuntime().addShutdownHook(new Thread("GBWScannerMain-shutdown-hook") {
            @Override
            public void run() {
                System.err.println("GBWScannerMain exit -------------------------------------------------------------------------------kao");;
            }
        });

        KeepAliveThread kpt = new KeepAliveThread("GBWScannerMain");

        kpt.start();
    }
}
