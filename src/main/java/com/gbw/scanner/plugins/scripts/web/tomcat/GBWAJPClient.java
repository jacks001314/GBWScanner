package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.Host;
import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.connection.GBWConnection;
import com.gbw.scanner.connection.SSLSocketClient;
import com.gbw.scanner.connection.SocketClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GBWAJPClient {

    private Connection connection;
    private GBWScanAJPConfig config;

    public GBWAJPClient(Host host,GBWScanAJPConfig config) throws GBWAJPConnectionException {

        this.config = config;
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

        connection.send(hmessage.getBuffer(),0,hmessage.getPos());

        if(bmessage!=null){
            connection.send(bmessage.getBuffer(),0,bmessage.getPos());
        }

    }

    public List<GBWAJPResponse> sendAndReceive(GBWAJPMessage hmessage,GBWAJPMessage bmessage) throws IOException {

        sendRequest(hmessage,bmessage);

        return readResponse();
    }

    public List<GBWAJPResponse> readResponse() throws IOException {

        List<GBWAJPResponse> responses = new ArrayList<>();
        while(true){

            try{

                GBWAJPResponse response = new GBWAJPResponse(connection,config.getMaxLen());
                if(response.getCode() == GBWAJPConstants.SEND_BODY_CHUNK){
                    responses.add(response);
                }
                if(response.getCode() == GBWAJPConstants.END_RESPONSE)
                    break;
            }catch (Exception e){
                break;
            }
        }

        return responses;
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
