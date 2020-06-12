package com.gbw.scanner.plugins.detect.tcp;

import com.gbw.scanner.Host;
import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.connection.GBWConnection;
import com.gbw.scanner.connection.SSLSocketClient;
import com.gbw.scanner.connection.SocketClient;
import com.gbw.scanner.plugins.detect.*;
import com.gbw.scanner.rule.GBWRuleMatch;
import com.gbw.scanner.utils.SSLUtils;
import com.xmap.api.utils.TextUtils;

import java.io.IOException;

public class GBWDetectTCP implements GBWDetect {

    private final GBWDetectConfig detectConfig;
    private final GBWDetectRuleConfig detectRuleConfig;

    public GBWDetectTCP(GBWDetectConfig detectConfig,GBWDetectRuleConfig detectRuleConfig){

        this.detectConfig = detectConfig;
        this.detectRuleConfig = detectRuleConfig;
    }

    @Override
    public GBWDetectResult detect(Host host, GBWDetectRule rule) throws GBWDetectException {

        GBWDetectResult result = null;

        String proto = rule.getProto();
        if(TextUtils.isEmpty(proto)||!proto.toLowerCase().startsWith("tcp")){

            throw  new GBWDetectException("Invalid tcp proto:"+proto);
        }

        SocketClient socketClient;
        if(proto.equalsIgnoreCase("tcps")){
            socketClient = new SSLSocketClient(true, SSLUtils.createSSLContext());
        }else{
            socketClient = new SocketClient();
        }
        try {
            socketClient.setDefaultTimeout(detectConfig.getDefaultTimeout());
            socketClient.setConnectTimeout(detectConfig.getConTimeout());
            socketClient.connect(host.getServer(),host.getPort());
            socketClient.setSoTimeout(detectConfig.getReadTimeout());

        } catch (Exception e) {
            throw new GBWDetectException("Cannot connect to host:"+host.getServer()+" port:"+host.getPort());
        }

        Connection connection = new GBWConnection(socketClient);
        GBWDetectConnection detectConnection = new GBWDetectConnection(connection);


        if(GBWRuleMatch.isMatch(detectConnection,rule)){
            result = new GBWDetectResult(host,rule,GBWDetectPlugin.DETECTTCP);
        }

        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public GBWDetectRuleConfig getRuleConfig() {
        return detectRuleConfig;
    }

}
