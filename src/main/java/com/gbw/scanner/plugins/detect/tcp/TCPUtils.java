package com.gbw.scanner.plugins.detect.tcp;

import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.connection.GBWConnection;
import com.gbw.scanner.connection.SSLSocketClient;
import com.gbw.scanner.connection.SocketClient;
import com.gbw.scanner.utils.ByteDataUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;


public class TCPUtils {

    private static void process(String ip,int port,String data,boolean isHex,int bsize,int timeout,boolean isSSL) {

        SocketClient socketClient;
        if(isSSL){
            socketClient = new SSLSocketClient();
        }else{
            socketClient = new SocketClient();
        }

        try {
            socketClient.setDefaultTimeout(timeout);
            socketClient.setConnectTimeout(timeout);
            socketClient.connect(ip,port);
            socketClient.setSoTimeout(timeout);

        } catch (Exception e) {
            System.out.println(String.format("Exception:%s:%d",ip,port));
            return;
        }

        Connection connection = new GBWConnection(socketClient);
        byte[] results = new byte[bsize];

        try {

            if(isHex){
                connection.send(ByteDataUtils.parseHex(data));
            }else{
                connection.send(data);
            }
            connection.read(results);
            System.out.println(String.format("%s:%d--->%s",ip,port,new String(results)));
        }catch (Exception e){

            System.out.println(String.format("When send/recv Exception:%s:%d",ip,port));
        }
        try {
            connection.close();
        }catch (Exception e){

        }
    }

    public static void main(String[] args) throws Exception{

        Options opts = new Options();
        String ip;
        int port;
        String data;
        int timeout = 10000;
        boolean ssl = false;
        boolean hex = false;

        int bsize = 1024;

        opts.addOption("host", true, "address<ip>:<port>");
        opts.addOption("timeout", true, "connect/read timeout in milliseconds");
        opts.addOption("data", true, "send data,hex/string");
        opts.addOption("ssl", false, "enable ssl");
        opts.addOption("hex", false, "hex");
        opts.addOption("bsize", true, "receive data buffer size");
        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){
            new HelpFormatter().printHelp("tcp-tool", opts);
            System.exit(0);
        }

        if(!cliParser.hasOption("host")||!cliParser.hasOption("data")){
            System.out.println("Please specify host and data");
            System.exit(-1);
        }

        String[] hp = cliParser.getOptionValue("host").split(":");
        ip = hp[0];
        port = Integer.parseInt(hp[1]);
        data = cliParser.getOptionValue("data");

        if(cliParser.hasOption("ssl"))
            ssl = true;

        if(cliParser.hasOption("hex"))
            hex = true;

        if(cliParser.hasOption("bsize"))
            bsize = Integer.parseInt(cliParser.getOptionValue("bsize"));

        if(cliParser.hasOption("timeout"))
            timeout = Integer.parseInt(cliParser.getOptionValue("timeout"));

        process(ip,port,data,hex,bsize,timeout,ssl);

    }

}
