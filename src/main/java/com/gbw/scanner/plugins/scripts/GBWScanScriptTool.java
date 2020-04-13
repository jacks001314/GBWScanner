package com.gbw.scanner.plugins.scripts;

import com.gbw.scanner.Host;
import com.gbw.scanner.utils.KeepAliveThread;
import com.xmap.api.utils.TextUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GBWScanScriptTool {

    private final GBWScanScript scanScript;
    private final LinkedBlockingQueue<Host> hosts;
    private  int threads;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private CommandLine cliParser;

    public GBWScanScriptTool(GBWScanScript scanScript,LinkedBlockingQueue<Host> hosts,int threads){

        this.scanScript = scanScript;
        this.hosts = hosts;
        this.threads = threads;
    }

    public GBWScanScriptTool(String[] args,GBWScanScript scanScript,int defaultPort) throws Exception{

        this.scanScript = scanScript;
        this.hosts = new LinkedBlockingQueue<>();
        this.threads = 1;

        init(new Options(),args,defaultPort);
    }

    public GBWScanScriptTool(String[] args,GBWScanScript scanScript,Options opts,int defaultPort) throws Exception{

        this.scanScript = scanScript;
        this.hosts = new LinkedBlockingQueue<>();
        this.threads = 1;

        init(opts,args,defaultPort);
    }

    private void init(Options opts,String[] args,int defaultPort) throws Exception{

        // Command line options
        int port = defaultPort;
        int timeout = 10000;
        GBWScanScriptCommonConfig config = scanScript.getConfig();

        opts.addOption("host", true, "address<ip>");
        opts.addOption("port", true, "port");
        opts.addOption("timeout", true, "connect/read timeout in milliseconds");
        opts.addOption("hosts", true, "addresses from file to read");
        opts.addOption("threads", true, "the number of threads");
        opts.addOption("help", false, "Print usage");

        cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("GBWScanScript", opts);
            System.exit(0);
        }

        if(cliParser.hasOption("port")){

            port = Integer.parseInt(cliParser.getOptionValue("port"));
        }

        if(cliParser.hasOption("host")){

            String h = cliParser.getOptionValue("host");

            Host host = new Host(h,h,port,null,null);

            hosts.put(host);
        }

        if(cliParser.hasOption("hosts")){

            String hostsFile = cliParser.getOptionValue("hosts");
            for(String line: Files.readAllLines(Paths.get(hostsFile))){

                if(TextUtils.isEmpty(line))
                    continue;

                Host host;

                if(line.indexOf(":")!=-1){
                    String[] hports = line.split(":");
                    host = new Host(hports[0],hports[0],Integer.parseInt(hports[1]),null,null);
                } else {
                    host = new Host(line,line,port,null,null);
                }

                hosts.put(host);
            }
        }

        if(cliParser.hasOption("timeout")){
            timeout = Integer.parseInt(cliParser.getOptionValue("timeout"));
        }

        if(cliParser.hasOption("threads")){

            threads = Integer.parseInt(cliParser.getOptionValue("threads"));
        }

        config.setConTimeout(timeout);
        config.setReadTimeout(timeout);
    }


    public CommandLine getCliParser() {
        return cliParser;
    }

    public void start(){

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(threads);

        for(int i = 0; i < threads; ++i) {
            try {
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new ScriptToolThread(), 0, 500, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*keepalive main thread*/
        Runtime.getRuntime().addShutdownHook(new Thread("GBWScriptTool-shutdown-hook") {
            @Override
            public void run() {
                System.err.println("GBWScannerMain exit -------------------------------------------------------------------------------kao");;
            }
        });

        KeepAliveThread kpt = new KeepAliveThread("GBWScriptTool");

        kpt.start();
    }

    private class ScriptToolThread implements Runnable{

        private boolean isPrint;
        public ScriptToolThread(){

            isPrint = false;
        }

        @Override
        public void run() {

            while(true){

                if(hosts.isEmpty()) {
                    if(!isPrint){

                        System.out.println("Scan End..............................");
                        isPrint = true;
                    }

                    break;
                }

                try {
                    Host host = hosts.take();
                    if(host!=null){

                        //System.out.println(String.format("start scan:%s:%d",host.getHost(),host.getPort()));
                        scanScript.scan(host,null);
                    }

                } catch (Exception e) {
                    //e.printStackTrace();
                    break;
                }
            }
        }


    }

}
