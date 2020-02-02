package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;

public class GBWRDPServerInfo {

    private int serverInfoTag; /*2 bytes*/
    private int serverInfoLen; /*2 bytes*/
    private int rdpVersion; /*2 bytes*/


    public GBWRDPServerInfo(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        serverInfoTag = buffer.readUInt16();
        serverInfoLen = buffer.readUInt16();
        rdpVersion = buffer.readUInt16();
        buffer.skip(2);
        buffer.skip(serverInfoLen-8);
    }


    public int getServerInfoTag() {
        return serverInfoTag;
    }


    public int getRdpVersion() {
        return rdpVersion;
    }

    public int getServerInfoLen() {
        return serverInfoLen;
    }

}
