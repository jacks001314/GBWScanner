package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.Host;
import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.connection.GBWConnection;
import com.gbw.scanner.connection.SSLSocketClient;
import com.gbw.scanner.connection.SocketClient;

import java.io.IOException;

public class GBWAJPClient {

    private Connection connection;

    public GBWAJPClient(Host host,GBWScanAJPConfig config) throws GBWAJPConnectionException {

        SocketClient socketClient;
        if(config.isSSL()){
            socketClient = new SSLSocketClient();
        }else{
            socketClient = new SocketClient();
        }
        try {
            socketClient.setDefaultTimeout(config.getConTimeout());
            socketClient.setConnectTimeout(config.getConTimeout());
            socketClient.connect(host.getServer(),host.getPort());
            socketClient.setSoTimeout(config.getReadTimeout());

        } catch (Exception e) {
            throw new GBWAJPConnectionException("Cannot connect to host:"+host.getServer()+" port:"+host.getPort());
        }
        connection = new GBWConnection(socketClient);
    }

    public void sendRequest(GBWAJPMessage hmessage,GBWAJPMessage bmessage) throws IOException {

        connection.send(hmessage.getBuffer(),0,hmessage.getLen());

        if(bmessage!=null){
            connection.send(bmessage.getBuffer(),0,bmessage.getLen());
        }

    }

    public byte[] sendAndReceive(GBWAJPMessage hmessage,GBWAJPMessage bmessage) throws IOException {

        sendRequest(hmessage,bmessage);

        return readResponse();
    }

    public byte[] readResponse() throws IOException {

        GBWAJPMessage message = new GBWAJPMessage(32);
        byte[]  b = message.getBuffer();
        connection.read(b,0,message.getHeaderLength());
        int mlen = message.processHeader();

        if(mlen<0){
            throw new IOException("Invalid AJP message length");
        }else if(mlen == 0){
            return "no content".getBytes();
        }

        byte[] res = new byte[mlen];
        connection.read(res,message.getHeaderLength(),mlen);

        return res;
    }

    public void close(){

        try {
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        connection = null;
    }


}
