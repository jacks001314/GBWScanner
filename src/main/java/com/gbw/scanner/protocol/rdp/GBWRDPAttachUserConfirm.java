package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;
import com.gbw.scanner.protocol.GBWEndian;

public class GBWRDPAttachUserConfirm {

    private GBWRDPHeader header;

    private int len;
    private int type;
    private int eot;

    private int tag;
    private int tlen;

    private int userId;

    public GBWRDPAttachUserConfirm(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        header = new GBWRDPHeader(buffer);
        len = buffer.readByte();
        type = buffer.readByte();
        eot = buffer.readByte();
        tag = buffer.readByte();

        tlen = buffer.readByte();

        userId = buffer.readUInt16(GBWEndian.BE);

    }

    public int getEot() {
        return eot;
    }

    public int getTlen() {
        return tlen;
    }

    public int getType() {
        return type;
    }


    public int getTag() {
        return tag;
    }

    public int getLen() {
        return len;
    }

    public int getUserId() {
        return userId;
    }

    public GBWRDPHeader getHeader() {
        return header;
    }
}
