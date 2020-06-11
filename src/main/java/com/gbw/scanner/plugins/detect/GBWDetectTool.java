package com.gbw.scanner.plugins.detect;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.detect.tcp.GBWDetectTCP;
import com.gbw.scanner.utils.GsonUtils;
import com.gbw.scanner.utils.KeepAliveThread;
import com.xmap.api.utils.TextUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GBWDetectTool {

    private GBWDetectConfig config;
    private GBWDetectRuleConfig ruleConfig;
    private GBWDetectRuleSplitQueue splitQueue;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private GBWDetect detect;

    public GBWDetectTool(String[] args) throws Exception {

        this.splitQueue = new GBWDetectRuleSplitQueue();
        this.config = new GBWDetectConfig();

        config.setConTimeout(10000);
        config.setDefaultTimeout(10000);
        config.setReadTimeout(10000);
        config.setThreads(1);

        init(args);
    }

    private void init(String[] args) throws Exception {


        Options opts = new Options();

        opts.addOption("threads",true,"detect thread numbers");
        opts.addOption("timeout",true,"connect/read timeout");
        opts.addOption("rulePath",true,"detect rule file path");
        opts.addOption("host",true,"bruteforce host");
        opts.addOption("hosts",true,"bruteforce hosts File");
        opts.addOption("port",true,"bruteforce port");

        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("GBWDetectTool:", opts);
            System.exit(0);
        }

        if(!cliParser.hasOption("rulePath")){

            System.out.println("Must specify detect rule path!");
            System.exit(-1);
        }

        this.ruleConfig = GBWDetectRuleConfigFactory.create(cliParser.getOptionValue("rulePath"));

        this.detect = new GBWDetectTCP(config,ruleConfig);

        if(cliParser.hasOption("threads")){

            config.setThreads(Integer.parseInt(cliParser.getOptionValue("threads")));
        }
        if(cliParser.hasOption("timeout")){

            int timeout = Integer.parseInt(cliParser.getOptionValue("timeout"));
            config.setConTimeout(timeout);
            config.setDefaultTimeout(timeout);
            config.setReadTimeout(timeout);
        }

        int port = 0;

        if(cliParser.hasOption("port")) {
            port = Integer.parseInt(cliParser.getOptionValue("port"));
        }

        if(cliParser.hasOption("host")){
            addHost(cliParser.getOptionValue("host"),port);
        }

        if(cliParser.hasOption("hosts")){

            for(String host: Files.readAllLines(Paths.get(cliParser.getOptionValue("hosts")))){

                host = host.trim();

                if(TextUtils.isEmpty(host))
                    continue;

                addHost(host,port);
            }
        }

    }


    public void start() {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(config.getThreads());

        for(int i = 0; i < config.getThreads(); ++i) {
            try {
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new GBWDetectThread(splitQueue,null), 0, 500, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void addHost(String h,int dport){
        int port = dport;
        String host = h;

        if(host.indexOf(":")!=-1){

            String[] hp = host.split(":");
            host = hp[0];
            port = Integer.parseInt(hp[1]);
        }

        doDetectRuleSplit(detect,new Host(host,host,port,null,"tcp"));
    }

    private void doDetectRuleSplit(GBWDetect detect, Host host){

        GBWDetectRuleConfig ruleConfig = detect.getRuleConfig();
        int n = config.getThreads();
        int dn = ruleConfig.getRulesNumber();
        int dv = dn/n;

        if(dv<=n||dv==dn){
            splitQueue.put(new GBWDetectRuleSplit(detect,host,0,dn-1));
        }else{

            GBWDetectRuleSplit split =null;
            int dd = 0;

            while(dd<dn){

                if(dd+dv>=dn){

                    split.setEnd(dn-1);
                    break;
                }

                split = new GBWDetectRuleSplit(detect,host,dd,dd+dv-1);
                dd+=dv;

                splitQueue.put(split);
            }
        }
    }


    public static void main(String[] args) throws Exception {


        GBWDetectTool detectTool = new GBWDetectTool(args);

        detectTool.start();

        /*keepalive main thread*/
        Runtime.getRuntime().addShutdownHook(new Thread("GBWDetectTool-shutdown-hook") {
            @Override
            public void run() {
                System.err.println("GBWDetectTool exit -------------------------------------------------------------------------------kao");;
            }
        });

        KeepAliveThread kpt = new KeepAliveThread("GBWDetectTool");

        kpt.start();
    }

}
