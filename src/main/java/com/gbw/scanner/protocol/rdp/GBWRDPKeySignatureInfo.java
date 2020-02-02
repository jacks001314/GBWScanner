package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;

public class GBWRDPKeySignatureInfo {

    private int tag;
    private int len;

    public GBWRDPKeySignatureInfo(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {


        tag = buffer.readUInt16();
        len = buffer.readUInt16();

        /*unkown*/
        buffer.skip(len);
    }

    public int getLen() {
        return len;
    }

    public int getTag() {
        return tag;
    }
}
