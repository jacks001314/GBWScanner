package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.Host;
import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.connection.GBWConnection;
import com.gbw.scanner.connection.SSLSocketClient;
import com.gbw.scanner.connection.SocketClient;
import com.gbw.scanner.utils.SSLUtils;
import com.xmap.api.utils.TextUtils;

public class GBWWeblogicConnection {

    public static Connection makeConnection(GBWScanWeblogicConfig config,Host host){

        String proto = host.getProto();
        boolean isSSL = false;

        if(!TextUtils.isEmpty(proto))
            isSSL = proto.equalsIgnoreCase("t3s")||proto.equals("https");

        SocketClient socketClient;
        if(isSSL){

            socketClient = new SSLSocketClient(true, SSLUtils.createSSLContext());
        }else{
            socketClient = new SocketClient();
        }

        try {
            socketClient.setDefaultTimeout(config.getConTimeout());
            socketClient.setConnectTimeout(config.getConTimeout());
            socketClient.connect(host.getHost(),host.getPort());
            socketClient.setSoTimeout(config.getReadTimeout());
        } catch (Exception e) {
            return null;
        }

        Connection connection = new GBWConnection(socketClient);

        return connection;
    }


}
