package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;
import com.gbw.scanner.utils.ByteDataUtils;

public class GBWRDPBerInfo {

    private int tag;
    private int len;
    private int value;

    public GBWRDPBerInfo(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        tag = buffer.readByte();
        len = buffer.readByte();
        value = buffer.readByte();
    }

    public GBWRDPBerInfo(GBWBuffer<?> buffer,int bytes) throws GBWBuffer.GBWBufferException {

        tag = buffer.readByte();
        len = buffer.readByte();

        value = ByteDataUtils.toBigInt(buffer.readRawBytes(bytes),true,false).intValue();
    }

    public int getLen() {
        return len;
    }

    public int getTag() {
        return tag;
    }

    public int getValue() {
        return value;
    }

}
