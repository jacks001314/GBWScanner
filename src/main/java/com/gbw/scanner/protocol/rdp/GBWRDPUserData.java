package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;
import com.gbw.scanner.utils.ByteDataUtils;

public class GBWRDPUserData {

    private int berTagOctString; /*1 byte*/
    private int len;

    private GBWRDPServerInfo serverInfo;
    private GBWRDPServerChannelInfo serverChannelInfo;
    private GBWRDPServerEncryptInfo serverEncryptInfo;


    public GBWRDPUserData(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        berTagOctString = buffer.readByte();
        len = ByteDataUtils.toBigInt(buffer.readRawBytes(3),true,false).intValue();

        buffer.skip(23);

        serverInfo = new GBWRDPServerInfo(buffer);
        serverChannelInfo = new GBWRDPServerChannelInfo(buffer);
        serverEncryptInfo = new GBWRDPServerEncryptInfo(buffer);

    }

    public GBWRDPServerEncryptInfo getServerEncryptInfo() {
        return serverEncryptInfo;
    }

    public GBWRDPServerChannelInfo getServerChannelInfo() {
        return serverChannelInfo;
    }

    public int getLen() {
        return len;
    }

    public GBWRDPServerInfo getServerInfo() {
        return serverInfo;
    }

    public int getBerTagOctString() {
        return berTagOctString;
    }
}
