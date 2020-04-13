package com.gbw.scanner.plugins.scripts.redis;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptTool;
import com.gbw.scanner.sink.SinkQueue;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class GBWScanRedisScript implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanRedisScript.class);

    private GBWScanRedisConfig config;

    public GBWScanRedisScript(GBWScanRedisConfig config){

        this.config = config;
    }

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }

    private boolean isWritable(Jedis jedis,String dir){

        try {

            jedis.configSet("dir",dir);
            jedis.set("x","scanRedisTest");
            jedis.configSet("dbfilename","ScanRedis");
            jedis.save();
            return true;
        }catch (Exception e){

            return false;
        }

    }

    private void checkDirWritable(Jedis jedis,GBWScanRedisResult redisResult){

        for(String dir:config.getDirs()){

            if(isWritable(jedis,dir)){
                redisResult.addDir(dir);
            }
        }

    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {
        Jedis jedis =null;

        try {

            jedis = new Jedis(host.getHost(),host.getPort(),config.getConTimeout());
            String info = jedis.info();
            if(info.contains(config.getKey())){

                GBWScanRedisResult redisResult = new GBWScanRedisResult(config,host);

                if(config.isCheckDir()){

                    checkDirWritable(jedis,redisResult);
                }

                if(sinkQueue!=null){
                    sinkQueue.put(redisResult);
                    log.warn(redisResult.toString());
                }else{

                    System.out.println(redisResult);
                }
            }
        }catch (Exception e){

        }finally {

            if(jedis!=null){
                jedis.close();
            }
        }

    }

    public static void main(String[] args) throws Exception {

        GBWScanRedisConfig config = new GBWScanRedisConfig();
        GBWScanRedisScript scanRedisScript = new GBWScanRedisScript(config);

        Options opts = new Options();

        opts.addOption("key",true,"redis info key");
        opts.addOption("dirs",true,"redis check writable dirs,args:<dir1>:<dir2>....");
        opts.addOption("checkDir",false,"will check dir is writable");

        GBWScanScriptTool tool = new GBWScanScriptTool(args,scanRedisScript,opts,6379);

        CommandLine cli = tool.getCliParser();

        List<String> dirs = new ArrayList<>();

        if(cli.hasOption("key")){

            config.setKey(cli.getOptionValue("key"));
        }else{
            config.setKey("redis_version");
        }

        if(cli.hasOption("dirs")){
            String[] splits = cli.getOptionValue("dirs").split(":");

            for(String s:splits)
                dirs.add(s);

        }else {

            dirs.add("/var/spool/cron");
            dirs.add("/etc/cron.d");
            dirs.add("/root/.ssh");
            dirs.add("/home/redis/.ssh");
        }

        config.setCheckDir(cli.hasOption("checkDir"));

        config.setDirs(dirs);

        tool.start();
    }


}
