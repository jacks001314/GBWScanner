package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;
import com.gbw.scanner.protocol.GBWEndian;

public class GBWRDPHeader {

    private int version; /*1 byte*/
    private int reserved; /*1 byte*/
    private int len; /*2 byte*/


    public GBWRDPHeader(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        version = buffer.readByte();
        reserved = buffer.readByte();
        len = buffer.readUInt16(GBWEndian.BE);
    }

    public int getLen() {
        return len;
    }

    public int getReserved() {
        return reserved;
    }

    public int getVersion() {
        return version;
    }
}
