package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;

public class GBWRDPPublicKeyInfo {

    private int tag;
    private int len;
    private byte[] magic;
    private long modulusLen;
    private byte[] exponent;

    private byte[] modulus;

    public GBWRDPPublicKeyInfo(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        tag = buffer.readUInt16();
        len = buffer.readUInt16();

        magic = new byte[4];
        buffer.readRawBytes(magic);

        modulusLen = buffer.readUInt32()-8;
        /*unkown*/
        buffer.skip(8);
        exponent = new byte[4];
        buffer.readRawBytes(exponent);

        modulus = new byte[(int)modulusLen];
        buffer.readRawBytes(modulus);
        buffer.skip(8);
    }

    public byte[] getModulus() {
        return modulus;
    }

    public byte[] getExponent() {
        return exponent;
    }

    public byte[] getMagic() {
        return magic;
    }

    public long getModulusLen() {
        return modulusLen;
    }

    public int getLen() {
        return len;
    }

    public int getTag() {
        return tag;
    }

}
