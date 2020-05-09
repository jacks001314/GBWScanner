package com.gbw.scanner.plugins.bruteforce;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.bruteforce.ftp.GBWBruteForceFTP;
import com.gbw.scanner.plugins.bruteforce.ftp.GBWBruteForceFTPConfig;
import com.gbw.scanner.plugins.bruteforce.mail.GBWBruteForceMail;
import com.gbw.scanner.plugins.bruteforce.mail.GBWBruteForceMailConfig;
import com.gbw.scanner.plugins.bruteforce.mssql.GBWBruteForceMSSQL;
import com.gbw.scanner.plugins.bruteforce.mssql.GBWBruteForceMSSQLConfig;
import com.gbw.scanner.plugins.bruteforce.mysql.GBWBruteForceMySQL;
import com.gbw.scanner.plugins.bruteforce.mysql.GBWBruteForceMySQLConfig;
import com.gbw.scanner.plugins.bruteforce.redis.GBWBruteForceRedis;
import com.gbw.scanner.plugins.bruteforce.redis.GBWBruteForceRedisConfig;
import com.gbw.scanner.plugins.bruteforce.ssh.GBWBruteForceSSH;
import com.gbw.scanner.plugins.bruteforce.ssh.GBWBruteForceSSHConfig;
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

public class GBWBruteForceTool {

    private GBWBruteForceConfig config;
    private GBWDictSplitQueue splitQueue;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private GBWBruteForce bruteForce;
    private String proto;

    public GBWBruteForceTool(String[] args) throws Exception {

        this.splitQueue = new GBWDictSplitQueue();
        this.config = new GBWBruteForceConfig();
        this.bruteForce = null;
        this.proto = null;
        String type;
        int threads = 1;

        if(!args[1].equals("--type")){
            System.out.println("Userage: --type <ssh,ftp,mail,redis,mssql,mysql> ----");
            System.exit(-1);
        }

        type = args[2];

        Options opts = new Options();
        opts.addOption("threads",true,"brute force thread numbers");
        opts.addOption("type",true,"brute force types(ssh,ftp,mail,redis,mssql,mysql)");
        opts.addOption("proto",true,"brute force proto(ssh,ftp[s],smtp[s],pop[s],imap[s],redis,mssql,mysql)");
        opts.addOption("host",true,"bruteforce host");
        opts.addOption("hosts",true,"bruteforce hosts File");
        opts.addOption("port",true,"bruteforce port");

        opts.addOption("help", false, "Print usage");



        initBruteForce(type,opts,args);

    }

    private void initBruteForce(String type,Options opts,String[] args) throws Exception{

        CommandLine cliParser = null;

        if(type.equals("ssh")){

            GBWBruteForceSSHConfig sshConfig = new GBWBruteForceSSHConfig();
            sshConfig.addOpts(opts);
            cliParser = new GnuParser().parse(opts, args);
            if(cliParser.hasOption("help")){

                new HelpFormatter().printHelp("GBWBruteForce:"+type, opts);
                System.exit(0);
            }
            sshConfig.initFromOpts(cliParser);
            bruteForce = new GBWBruteForceSSH(sshConfig);
            config.setSshConfig(sshConfig);
            if(TextUtils.isEmpty(proto))
                proto = "ssh";

        }else if(type.equals("ftp")){

            GBWBruteForceFTPConfig ftpConfig = new GBWBruteForceFTPConfig();
            ftpConfig.addOpts(opts);
            cliParser = new GnuParser().parse(opts, args);
            if(cliParser.hasOption("help")){

                new HelpFormatter().printHelp("GBWBruteForce:"+type, opts);
                System.exit(0);
            }
            ftpConfig.initFromOpts(cliParser);
            bruteForce = new GBWBruteForceFTP(ftpConfig);
            config.setFtpConfig(ftpConfig);
            if(TextUtils.isEmpty(proto))
                proto = "ftp";
        }else if(type.equals("mail")){

            GBWBruteForceMailConfig mailConfig = new GBWBruteForceMailConfig();
            mailConfig.addOpts(opts);
            cliParser = new GnuParser().parse(opts, args);
            if(cliParser.hasOption("help")){

                new HelpFormatter().printHelp("GBWBruteForce:"+type, opts);
                System.exit(0);
            }
            mailConfig.initFromOpts(cliParser);
            bruteForce = new GBWBruteForceMail(mailConfig);
            config.setMailConfig(mailConfig);
            if(TextUtils.isEmpty(proto))
                proto = "smtp";

        }else if(type.equals("redis")){

            GBWBruteForceRedisConfig redisConfig = new GBWBruteForceRedisConfig();
            redisConfig.addOpts(opts);
            cliParser = new GnuParser().parse(opts, args);
            if(cliParser.hasOption("help")){

                new HelpFormatter().printHelp("GBWBruteForce:"+type, opts);
                System.exit(0);
            }
            redisConfig.initFromOpts(cliParser);
            bruteForce = new GBWBruteForceRedis(redisConfig);
            config.setRedisConfig(redisConfig);
            if(TextUtils.isEmpty(proto))
                proto = "redis";
        }else if(type.equals("mssql")){

            GBWBruteForceMSSQLConfig mssqlConfig = new GBWBruteForceMSSQLConfig();
            mssqlConfig.addOpts(opts);
            cliParser = new GnuParser().parse(opts, args);
            if(cliParser.hasOption("help")){

                new HelpFormatter().printHelp("GBWBruteForce:"+type, opts);
                System.exit(0);
            }
            mssqlConfig.initFromOpts(cliParser);
            bruteForce = new GBWBruteForceMSSQL(mssqlConfig);
            config.setMssqlConfig(mssqlConfig);
            if(TextUtils.isEmpty(proto))
                proto = "mssql";
        }else if(type.equals("mysql")){

            GBWBruteForceMySQLConfig mySQLConfig = new GBWBruteForceMySQLConfig();
            mySQLConfig.addOpts(opts);
            cliParser = new GnuParser().parse(opts, args);
            if(cliParser.hasOption("help")){

                new HelpFormatter().printHelp("GBWBruteForce:"+type, opts);
                System.exit(0);
            }
            mySQLConfig.initFromOpts(cliParser);
            bruteForce = new GBWBruteForceMySQL(mySQLConfig);
            config.setMySQLConfig(mySQLConfig);
            if(TextUtils.isEmpty(proto))
                proto = "mysql";
        }else{
            new HelpFormatter().printHelp("GBWBruteForce:"+type, opts);
            throw new IllegalArgumentException("Invalid type:"+type);
        }

        if(!cliParser.hasOption("help")&&(!cliParser.hasOption("port")||(!cliParser.hasOption("host")&&(!cliParser.hasOption("hosts"))))){

            throw new IllegalArgumentException("Musty specify bruteforce host[s] and port");
        }

        int threads =1;

        if(cliParser.hasOption("threads"))
            threads = Integer.parseInt(cliParser.getOptionValue("threads"));

        if(cliParser.hasOption("proto"))
            this.proto = cliParser.getOptionValue("proto");

        config.setThreads(threads);

        int port = Integer.parseInt(cliParser.getOptionValue("port"));

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

    private void addHost(String h,int dport){
        int port = dport;
        String host = h;

        if(host.indexOf(":")!=-1){

            String[] hp = host.split(":");
            host = hp[0];
            port = Integer.parseInt(hp[1]);
        }

        doDictSplit(bruteForce,new Host(host,host,port,null,proto));
    }

    public void start() {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(config.getThreads());

        for(int i = 0; i < config.getThreads(); ++i) {
            try {
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new GBWBruteForceThread(splitQueue,null), 0, 1, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void doDictSplit(GBWBruteForce bruteForce,Host host){

        GBWDict dict = bruteForce.getDict();
        int n = config.getThreads();
        int dn = dict.getDictEntries().size();
        int dv = dn/n;

        if(dv<=n||dv==dn){
            splitQueue.put(new GBWDictSplit(bruteForce,host,0,dn-1));
        }else{

            GBWDictSplit split =null;
            int dd = 0;

            while(dd<dn){

                if(dd+dv>=dn){

                    split.setEnd(dn-1);
                    break;
                }

                split = new GBWDictSplit(bruteForce,host,dd,dd+dv-1);
                dd+=dv;

                splitQueue.put(split);
            }
        }
    }


    public static void main(String[] args) throws Exception {

        GBWBruteForceTool tool = new GBWBruteForceTool(args);

        tool.start();

        /*keepalive main thread*/
        Runtime.getRuntime().addShutdownHook(new Thread("GBWBruteForce-shutdown-hook") {
            @Override
            public void run() {
                System.err.println("GBWBruteForce exit -------------------------------------------------------------------------------kao");;
            }
        });

        KeepAliveThread kpt = new KeepAliveThread("GBWBruteForceTool");

        kpt.start();
    }


}
