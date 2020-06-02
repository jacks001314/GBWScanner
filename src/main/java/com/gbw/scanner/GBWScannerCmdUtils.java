package com.gbw.scanner;

import com.gbw.scanner.cmd.GBWCmdMessage;
import com.gbw.scanner.utils.GsonUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.commons.cli.*;
import redis.clients.jedis.Jedis;

public class GBWScannerCmdUtils {

    public static void runCmd(Jedis jedis, String channel, String cmd) {

        GBWCmdMessage cmdMessage = new GBWCmdMessage();

        cmdMessage.setName(cmd);
        cmdMessage.setContent("");

        jedis.publish(channel, GsonUtils.toJson(cmdMessage,false));

    }

    public static void main(String[] args) throws ParseException {

        String host = "127.0.0.1";
        int port = 6379;
        String auth = "AntellSec#2017";
        String channel = "GBWCmd";
        String cmd = "";

        Options opts = new Options();

        opts.addOption("host",true,"set redis host");
        opts.addOption("port",true,"set redis port");
        opts.addOption("auth",true,"set redis auth passwd");
        opts.addOption("channel",true,"set redis channel name");
        opts.addOption("cmd",true,"set cmd---[exit,]");

        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("GBWHostSourceCmdUtils:", opts);
            System.exit(0);
        }

        if(cliParser.hasOption("host")){
            host = cliParser.getOptionValue("host");
        }

        if(cliParser.hasOption("port")){
            port = Integer.parseInt(cliParser.getOptionValue("port"));
        }
        if(cliParser.hasOption("auth")){
            auth = cliParser.getOptionValue("auth");
        }
        if(cliParser.hasOption("channel")){
            channel = cliParser.getOptionValue("channel");
        }
        if(cliParser.hasOption("cmd"))
            cmd = cliParser.getOptionValue("cmd");

        Jedis jedis = new Jedis(host,port);
        if(!TextUtils.isEmpty(auth))
            jedis.auth(auth);

        System.out.println(String.format("Run a cmd:%s",cmd));

        runCmd(jedis,channel,cmd);

        jedis.close();

    }


}
