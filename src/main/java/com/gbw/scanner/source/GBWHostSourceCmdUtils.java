package com.gbw.scanner.source;

import com.gbw.scanner.cmd.GBWCmdConstants;
import com.gbw.scanner.cmd.GBWCmdMessage;
import com.gbw.scanner.utils.Base64Utils;
import com.gbw.scanner.utils.GsonUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.commons.cli.*;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GBWHostSourceCmdUtils {


    public static void addSource(Jedis jedis,String channel,String type,String json){

        GBWCmdMessage cmdMessage = new GBWCmdMessage();
        cmdMessage.setName(GBWCmdConstants.addSourceCmd);

        GBWHostSourceCmdMesage sourceCmdMesage = new GBWHostSourceCmdMesage();

        sourceCmdMesage.setType(type);
        sourceCmdMesage.setContent(Base64Utils.encode(json));

        cmdMessage.setContent(GsonUtils.toJson(sourceCmdMesage,true));
        jedis.publish(channel,GsonUtils.toJson(cmdMessage,false));

    }

    public static void main(String[] args) throws ParseException, IOException {

        String host = "127.0.0.1";
        int port = 6379;
        String auth = "";
        String jsonFile;
        String json = "";
        Jedis jedis = null;
        String type = "fLine";
        String channel = "GBWCmd";

        Options opts = new Options();


        opts.addOption("host",true,"set redis host");
        opts.addOption("port",true,"set redis port");
        opts.addOption("auth",true,"set redis auth passwd");
        opts.addOption("channel",true,"set redis channel name");

        opts.addOption("type",true,"set host source type");
        opts.addOption("json",true,"set host source config json File");
        opts.addOption("isBase64",false,"json content is base64");

        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("GBWHostSourceCmdUtils:", opts);
            System.exit(0);
        }

        if(!cliParser.hasOption("type")||!cliParser.hasOption("json"))
        {
            System.out.println("Must specify type and json content!");
            System.exit(-1);
        }


        type = cliParser.getOptionValue("type");
        jsonFile = cliParser.getOptionValue("json");
        json = new String(Files.readAllBytes(Paths.get(jsonFile)));

        if(cliParser.hasOption("isBase64"))
            json = Base64Utils.decode(json);

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

        jedis = new Jedis(host,port);
        if(!TextUtils.isEmpty(auth))
            jedis.auth(auth);

        System.out.println(String.format("Add a Source,type:%s,config json:%s",type,json));

        addSource(jedis,channel,type,json);

        jedis.close();
    }

}
