package com.gbw.scanner.plugins.bruteforce;

import com.gbw.scanner.GBWScannerPlugin;
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
import com.gbw.scanner.sink.SinkQueue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GBWBruteForcePlugin implements GBWScannerPlugin {

    public static final String BRUTEFORCEFTP = "bruteForceFTP";
    public static final String BRUTEFORCEMAIL = "bruteForceMail";
    public static final String BRUTEFORCEMYSQL = "bruteForceMYSQL";
    public static final String BRUTEFORCEMSSQL = "bruteForceMSSQL";
    public static final String BRUTEFORCEREDIS = "bruteForceRedis";
    public static final String BRUTEFORCESSH = "bruteForceSSH";

    private final Map<String,GBWBruteForce> bruteForceMap;
    private final GBWBruteForceConfig config;
    private final GBWDictSplitQueue splitQueue;
    private final SinkQueue sinkQueue;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public GBWBruteForcePlugin(GBWBruteForceConfig config,SinkQueue sinkQueue) throws Exception {

        this.bruteForceMap = new HashMap<>();
        this.config = config;
        this.splitQueue = new GBWDictSplitQueue();
        this.sinkQueue = sinkQueue;

        GBWBruteForceFTPConfig ftpConfig = config.getFtpConfig();

        if(ftpConfig.isOn()){
            GBWBruteForceFTP bruteForceFTP = new GBWBruteForceFTP(ftpConfig);
            if(bruteForceFTP.getDict().dictEntries.size()>0){
                bruteForceMap.put(BRUTEFORCEFTP,bruteForceFTP);
            }
        }

        GBWBruteForceMailConfig mailConfig = config.getMailConfig();
        if(mailConfig.isOn()){

            GBWBruteForceMail bruteForceMail = new GBWBruteForceMail(mailConfig);
            if(bruteForceMail.getDict().dictEntries.size()>0){
                bruteForceMap.put(BRUTEFORCEMAIL,bruteForceMail);
            }
        }

        GBWBruteForceMySQLConfig mySQLConfig = config.getMySQLConfig();
        if(mySQLConfig.isOn()){

            GBWBruteForceMySQL bruteForceMySQL = new GBWBruteForceMySQL(mySQLConfig);
            if(bruteForceMySQL.getDict().dictEntries.size()>0){
                bruteForceMap.put(BRUTEFORCEMYSQL,bruteForceMySQL);
            }
        }

        GBWBruteForceMSSQLConfig mssqlConfig = config.getMssqlConfig();
        if(mssqlConfig.isOn()){

            GBWBruteForceMSSQL bruteForceMSSQL = new GBWBruteForceMSSQL(mssqlConfig);

            if(bruteForceMSSQL.getDict().dictEntries.size()>0){
                bruteForceMap.put(BRUTEFORCEMSSQL,bruteForceMSSQL);
            }
        }

        GBWBruteForceRedisConfig redisConfig = config.getRedisConfig();
        if(redisConfig.isOn()){

            GBWBruteForceRedis bruteForceRedis = new GBWBruteForceRedis(redisConfig);

            if(bruteForceRedis.getDict().dictEntries.size()>0){
                bruteForceMap.put(BRUTEFORCEREDIS,bruteForceRedis);
            }
        }

        GBWBruteForceSSHConfig sshConfig = config.getSshConfig();

        if(sshConfig.isOn()){

            GBWBruteForceSSH bruteForceSSH = new GBWBruteForceSSH(sshConfig);

            if(bruteForceSSH.getDict().dictEntries.size()>0){
                bruteForceMap.put(BRUTEFORCESSH,bruteForceSSH);
            }
        }


    }

    @Override
    public void start() {

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(config.getThreads());

        for(int i = 0; i < config.getThreads(); ++i) {
            try {
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new GBWBruteForceThread(splitQueue,sinkQueue), 0, 1, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean accept(Host host) {

        List<String> scanTypes = host.getTypes();

        if(scanTypes == null ||scanTypes.isEmpty())
            return false;

        for(String scanType:scanTypes){

            if(bruteForceMap.containsKey(scanType))
                return true;

        }

        return false;
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

    @Override
    public void process(Host host) {

        List<String> scanTypes = host.getTypes();

        if(scanTypes == null ||scanTypes.isEmpty())
            return;

        for(String scanType:scanTypes){

            GBWBruteForce bruteForce = bruteForceMap.get(scanType);

            if(bruteForce!=null){

                doDictSplit(bruteForce,host);
            }

        }

    }

}
