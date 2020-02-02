package com.gbw.scanner.plugins.detect.tcp;

import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.connection.GBWConnection;
import com.gbw.scanner.connection.SSLSocketClient;
import com.gbw.scanner.connection.SocketClient;


public class TCPUtils {

    private static void process(String ip,int port,String data,boolean isSSL) {

        SocketClient socketClient;
        if(isSSL){
            socketClient = new SSLSocketClient();
        }else{
            socketClient = new SocketClient();
        }

        try {
            socketClient.setDefaultTimeout(10000);
            socketClient.setConnectTimeout(10000);
            socketClient.connect(ip,port);
            socketClient.setSoTimeout(10000);

        } catch (Exception e) {
            System.out.println(String.format("Exception:%s:%d",ip,port));
            return;
        }

        Connection connection = new GBWConnection(socketClient);

        byte[] results = new byte[128];

        try {
            connection.send(data);
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

        /*
        if(args.length<3){
            System.out.println("Usage: main <ipFname><port><dataFile>[isSSL]");
            System.exit(-1);
        }

        boolean isSSL = false;

        if(args.length>=4){
            isSSL = args[3].equals("true");
        }

        for(String ip: Files.readAllLines(Paths.get(args[0])) ){

            process(ip,Integer.parseInt(args[1]),String.format("%s\n\0",args[2]),isSSL);
        }

         */
      String json = "{\"id\":3333,\"jsonrpc\":\"2.0\",\"method\":\"keepalived\",\"params\":{\"id\":\"xmrig\"}}\n\0";
      String json1 = "{\"id\":3333,\"method\":\"mining.subscribe\",\"params\":[]}\n\0";

        String ip = "x-pool.ulimit-n.com";
        int port = 80;


        process(ip,port,json,false);

    }

}
