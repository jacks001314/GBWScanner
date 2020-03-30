package com.gbw.scanner.plugins.scripts.windows.rdp.bluekeep;

import com.gbw.scanner.Host;
import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.connection.GBWConnection;
import com.gbw.scanner.connection.SocketClient;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptTool;
import com.gbw.scanner.protocol.GBWProtoBuffer;
import com.gbw.scanner.protocol.rdp.GBWRDPAttachUserConfirm;
import com.gbw.scanner.protocol.rdp.GBWRDPMCSResponse;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.ByteDataUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GBWScanBluekeepScript implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanBluekeepScript.class);

    private GBWScanBluekeepScriptConfig config;
    private static final String hostname="eltons-dev";
    private static final String username="elton";

    public GBWScanBluekeepScript(GBWScanBluekeepScriptConfig config){

        this.config = config;
    }


    @Override
    public boolean isAccept(Host host) {
        return true;
    }

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    private void doCheck(Host host,SinkQueue sinkQueue,Connection connection,GBWRC4Key rc4) throws Exception {

        byte[] results = new byte[1024];
        byte[] payloads = ByteDataUtils.parseHex("0300000902f0802180");

        for(int i = 0;i<6;i++)
            connection.read(results);

        for(int i = 0;i<6;i++){

            byte[] pkt = GBWRDPRequestBuilder.makeEncrypteRequest("100000000300000000000000020000000000000000000000",rc4,"0800", "0000", "03ed");
            connection.send(pkt);

            pkt = GBWRDPRequestBuilder.makeEncrypteRequest("20000000030000000000000000000000020000000000000000000000000000000000000000000000",rc4,"0800", "0000", "03ed");
            connection.send(pkt);

            for(int j = 0;j<4;j++){

                connection.read(results);
                if(ByteDataUtils.contains(results,payloads)){
                    log.warn("Found bluekeep valnerable in Host:"+host.getIp()+":"+host.getPort());
                    System.out.println("Found bluekeep valnerable in Host:"+host.getIp()+":"+host.getPort());
                    if(sinkQueue!=null) {
                        GBWScanBluekeepScriptResult bluekeepScriptResult = new GBWScanBluekeepScriptResult(config, host);
                        bluekeepScriptResult.setPayload(ByteDataUtils.toHex(results).substring(0, 64));
                        sinkQueue.put(bluekeepScriptResult);
                    }

                    return;
                }else {
                    System.out.println(String.format("no vul:%s:%d", host.getHost(), host.getPort()));
                }
            }
        }

    }

    @Override
    public void scan(Host host, SinkQueue sinkQueue) {

        String ip = host.getIp();
        int port = host.getPort();

        SocketClient socketClient = new SocketClient();

        try {
            socketClient.setDefaultTimeout(config.getConTimeout());
            socketClient.setConnectTimeout(config.getConTimeout());
            socketClient.connect(ip,port);
            socketClient.setSoTimeout(config.getReadTimeout());

        } catch (Exception e) {
            System.out.println(String.format("Connect to ip:%s:%d,error%s",host.getHost(),host.getPort(),e.getMessage()));
            return;
        }
        Connection connection = null;

        try{
            connection = new GBWConnection(socketClient);
            byte[] results = new byte[8192];


            /*send connect request */
            connection.send(GBWRDPRequestBuilder.makeConnectRequest(username));
            connection.read(results);

            /*send MCS Connect Initial PDU with GCC Conference*/
            connection.send(GBWRDPRequestBuilder.makeMCSGCCRequest(hostname));
            connection.read(results);
            /*parse MCS Response PDU with GCC Conference Received*/
            GBWRDPMCSResponse response = new GBWRDPMCSResponse(new GBWProtoBuffer(results));

            /*Sending MCS Erect Request*/
            connection.send(GBWRDPRequestBuilder.makeMCSErectDomainRequest());

            /*Sending MCS Attach User PDU Request*/
            connection.send(GBWRDPRequestBuilder.makeMSCAttachUserRequest());
            connection.read(results);

            /*parse user confirm info*/
            GBWRDPAttachUserConfirm confirm = new GBWRDPAttachUserConfirm(new GBWProtoBuffer(results));

            log.info("Send PDU  Request for 7 channel with AttachUserConfirm::initiator:"+confirm.getUserId());

            connection.send(GBWRDPRequestBuilder.makeChannelRequest(confirm.getUserId(),1009));
            connection.read(results);

            connection.send(GBWRDPRequestBuilder.makeChannelRequest(confirm.getUserId(),1003));
            connection.read(results);

            connection.send(GBWRDPRequestBuilder.makeChannelRequest(confirm.getUserId(),1004));
            connection.read(results);

            connection.send(GBWRDPRequestBuilder.makeChannelRequest(confirm.getUserId(),1005));
            connection.read(results);

            connection.send(GBWRDPRequestBuilder.makeChannelRequest(confirm.getUserId(),1006));
            connection.read(results);

            connection.send(GBWRDPRequestBuilder.makeChannelRequest(confirm.getUserId(),1007));
            connection.read(results);

            connection.send(GBWRDPRequestBuilder.makeChannelRequest(confirm.getUserId(),1008));
            connection.read(results);

            /*Sending security exchange PDU*/
            GBWEncryInfo encryInfo = new GBWEncryInfo(response);
            connection.send(GBWRDPRequestBuilder.makeSecurityExchangeRequest(encryInfo));

            GBWRC4Key rc4 = new GBWRC4Key(encryInfo);

            log.info("RC4_ENC_KEY:"+rc4.getInitialClientEncryptKey128());
            log.info("RC4_DEC_KEY:"+rc4.getInitialClientDecryptKey128());
            log.info("HMAC_KEY:"+rc4.getMacKey());
            log.info("SESS_BLOB:"+rc4.getSessionKeyBlob());

            /*Sending encrypted client info PDU*/
            connection.send(GBWRDPRequestBuilder.makeEncrypteRequest(GBWRDPRequestBuilder.makeClientData(),rc4,"4800","0000","03eb"));
            connection.read(results);
            connection.read(results);

            /*Sending client confirm active PDU*/
            connection.send(GBWRDPRequestBuilder.makeEncrypteRequest(GBWRDPRequestBuilder.makeClientConfirmActiveData(),rc4,"3800","0000","03eb"));

            //Sending client synchronize PDU
            //Sending client control cooperate PDU

            byte[] synch = GBWRDPRequestBuilder.makeEncrypteRequest(GBWRDPRequestBuilder.makeClientSynData(),rc4,"0800", "0000", "03eb");
            byte[] coop = GBWRDPRequestBuilder.makeEncrypteRequest(GBWRDPRequestBuilder.makeClientControlCooperateData(),rc4,"0800", "0000", "03eb");
            connection.send(ArrayUtils.addAll(synch,coop));


            //Sending client control request control PDU
            connection.send(GBWRDPRequestBuilder.makeEncrypteRequest(GBWRDPRequestBuilder.makeClientControlAndControlData(),rc4,"0800", "0000", "03eb"));


            //Sending client persistent key list PDU
            connection.send(GBWRDPRequestBuilder.makeEncrypteRequest(GBWRDPRequestBuilder.makeClientPersistentKeylistData(),rc4,"0800", "0000", "03eb"));

            //Sending client font list PDU
            connection.send(GBWRDPRequestBuilder.makeEncrypteRequest(GBWRDPRequestBuilder.makeClientFontlistData(),rc4,"0800", "0000", "03eb"));

            doCheck(host,sinkQueue,connection,rc4);

        }catch (Exception e){
            System.out.println(String.format("scan %s:%d,error:%s",host.getHost(),host.getPort(),e.getMessage()));
        }finally {

            try {
                connection.close();
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) throws Exception {

        GBWScanBluekeepScriptConfig config = new GBWScanBluekeepScriptConfig();

        GBWScanBluekeepScript scanBluekeepScript = new GBWScanBluekeepScript(config);

        GBWScanScriptTool tool = new GBWScanScriptTool(args,scanBluekeepScript,3389);

        tool.start();
    }


}
