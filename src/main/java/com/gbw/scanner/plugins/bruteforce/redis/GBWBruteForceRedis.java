package com.gbw.scanner.plugins.bruteforce.redis;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.bruteforce.*;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class GBWBruteForceRedis extends GBWAbstractBruteForce {

    private GBWBruteForceRedisConfig redisConfig;

    public GBWBruteForceRedis(GBWBruteForceRedisConfig config) throws IOException, InterruptedException {
        super(config);
        redisConfig = config;
    }

    @Override
    public GBWBruteForceResult bruteForce(Host host, GBWDictEntry entry) {

        Jedis client = null;
        try {
            client = new Jedis(host.getServer(), host.getPort(), redisConfig.isSSL());
            String res = client.auth(entry.getPasswd());
            if(res.toLowerCase().contains("ok")){

                return new GBWBruteForceResult(entry,host,GBWBruteForcePlugin.BRUTEFORCEREDIS);
            }

        }catch (Exception e){
            return null;
        }finally {
            if(client!=null){
                client.close();
            }
        }

        return null;
    }

}
