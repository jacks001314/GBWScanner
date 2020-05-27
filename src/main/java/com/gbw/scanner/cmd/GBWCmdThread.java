package com.gbw.scanner.cmd;

import com.google.gson.Gson;
import com.xmap.api.utils.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;

public class GBWCmdThread extends Thread{

    private static final Logger log = LoggerFactory.getLogger(GBWCmdThread.class);

    private final List<GBWCmdHandle> handles;
    private final GBWCmdConfig config;


    private class CmdRedisSubscriber extends JedisPubSub {


        @Override
        public void onMessage(String channel, String message) {

            Gson gson = new Gson();
            GBWCmdMessage cmdMessage = gson.fromJson(message,GBWCmdMessage.class);

            GBWCmdHandle handle = findHandle(cmdMessage);

            if(handle!=null){

                log.info(String.format("Receive a cmd message from redis,name:%s,content:%s\n",cmdMessage.getName(),cmdMessage.getContent()));

                handle.handle(cmdMessage);
            }
        }

    }

    public GBWCmdThread(GBWCmdConfig config) {

        this.handles = new ArrayList<>();
        this.config = config;



    }

    public void registerHandle(GBWCmdHandle handle){

        handles.add(handle);
    }

    private GBWCmdHandle findHandle(GBWCmdMessage message){

        for(GBWCmdHandle handle:handles){

            if(handle.isAccept(message))
                return handle;
        }

        return null;
    }

    @Override
    public void run() {


        Jedis jedis = new Jedis(config.getHost(),config.getPort());

        if(!TextUtils.isEmpty(config.getAuth()))
            jedis.auth(config.getAuth());

        jedis.subscribe(new CmdRedisSubscriber(),config.getChannel());

    }

    public static void main(String[] args){

        GBWCmdConfig config = new GBWCmdConfig();
        config.setAuth("AntellSec#2017");
        config.setHost("192.168.1.151");
        config.setPort(6379);
        config.setChannel("GBWCmd");

        GBWCmdThread thread = new GBWCmdThread(config);
        thread.start();
    }
}
