package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;

public class GBWRDPServerChannelInfo {

    private int tag; /*2 bytes*/
    private int len; /*2 bytes*/

    public GBWRDPServerChannelInfo(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        tag = buffer.readUInt16();
        len = buffer.readUInt16();
        buffer.skip(len-4);
    }




    public int getLen() {
        return len;
    }

    public int getTag() {
        return tag;
    }
}
