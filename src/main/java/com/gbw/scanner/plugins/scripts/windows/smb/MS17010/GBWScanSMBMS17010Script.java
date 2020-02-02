package com.gbw.scanner.plugins.scripts.windows.smb.MS17010;

import com.gbw.scanner.Host;
import com.gbw.scanner.connection.Connection;
import com.gbw.scanner.connection.GBWConnection;
import com.gbw.scanner.connection.SocketClient;
import com.gbw.scanner.plugins.scripts.GBWScanScript;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.protocol.GBWProtoBuffer;
import com.gbw.scanner.sink.SinkQueue;
import com.gbw.scanner.utils.ByteDataUtils;
import com.gbw.scanner.utils.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWScanSMBMS17010Script implements GBWScanScript {

    private static final Logger log = LoggerFactory.getLogger(GBWScanSMBMS17010Script.class);
    private final GBWScanSMBMS17010ScriptConfig config;

    private static final byte[] invalid = ByteDataUtils.parseHex("080000c0");
    private static final byte[] denied = ByteDataUtils.parseHex("220000c0");
    private static final byte[] vul = ByteDataUtils.parseHex("050200c0");

    public GBWScanSMBMS17010Script(GBWScanSMBMS17010ScriptConfig config){

        this.config = config;
    }

    @Override
    public GBWScanScriptCommonConfig getConfig() {
        return config;
    }

    @Override
    public boolean isAccept(Host host) {
        return true;
    }

    private static final long calculateDoublepulsarXORKey(long s) {

        long x = (2 * s ^ (((s & 0xff00 | (s << 16)) << 8) | (((s >> 16) | s & 0xff0000) >> 8)));
        x = x & 0xffffffff;
        return x;
    }


    @Override
    public  void scan(Host host, SinkQueue sinkQueue) {

        String ip = host.getIp();
        int port = host.getPort();

        SocketClient socketClient = new SocketClient();

        try {
            socketClient.setDefaultTimeout(10000);
            socketClient.setConnectTimeout(10000);
            socketClient.connect(ip,port);
            socketClient.setSoTimeout(10000);

        } catch (Exception e) {
            return;
        }
        Connection connection = null;

        try {
            connection = new GBWConnection(socketClient);

            byte[] results = new byte[1024];
            GBWProtoBuffer smbBuffer = new GBWProtoBuffer(results);
            SMBHeader smbHeader = new SMBHeader();

            connection.send(GBWSMBRequestBuilder.makeNegotiateProtoRequest());
            connection.read(results);
            smbHeader.readFrom(smbBuffer);

            connection.send(GBWSMBRequestBuilder.makeSessionSetupAndXRequest());
            connection.read(results);
            smbBuffer.resetRPos();
            smbHeader.readFrom(smbBuffer);

            int userID = smbHeader.getUserID();

            smbBuffer.skip(9);
            String os = smbBuffer.readNullTerminatedString(Charsets.UTF_8);

            connection.send(GBWSMBRequestBuilder.makeTreeConnectAndxRequest(ip, userID));
            connection.read(results);
            smbBuffer.resetRPos();
            smbHeader.readFrom(smbBuffer);
            int treeId = smbHeader.getTreeID();
            int processId = smbHeader.getProcessID();
            int mutID = smbHeader.getMultiplexID();
            userID = smbHeader.getUserID();

            connection.send(GBWSMBRequestBuilder.makePeekNamedPipeReqeust(treeId, processId, userID, mutID));
            connection.read(results);
            smbBuffer.resetRPos();
            smbHeader.readFrom(smbBuffer);

            byte[] ntStatus = new byte[4];
            smbBuffer.resetRPos();
            smbBuffer.skip(9);
            smbBuffer.readRawBytes(ntStatus);

            if (ByteDataUtils.equals(ntStatus, vul)){

                log.warn(String.format("Host:%s has maybe MS17-010 vulnerable",ip));

                connection.send(GBWSMBRequestBuilder.makeTransRequest(treeId,processId,userID,mutID));
                connection.read(results);
                smbBuffer.resetRPos();
                smbHeader.readFrom(smbBuffer);

                long key = 0;
                boolean doublepulsar = false;

                if(smbHeader.getMultiplexID() == 0x0051){

                    key = calculateDoublepulsarXORKey(smbHeader.getSignature());
                    doublepulsar = true;
                    log.warn(String.format("Host:%s is likely infected with doublepulsar! key:%d",ip,key));

                }


                GBWScanSMBMS17010ScriptResult result = new GBWScanSMBMS17010ScriptResult(config,host);
                result.setKey(key);
                result.setDoublePulsar(doublepulsar);
                result.setOs(os);

                sinkQueue.put(result);
            }

        }catch (Exception e){

        }finally {
            try {
                if(connection!=null)
                    connection.close();
            }catch (Exception e){

            }

        }
    }

}
