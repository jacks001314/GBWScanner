package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;

public class GBWRDPServerEncryptInfo {

    private int tag;
    private int len;
    private long rc4KeySize;
    private long encrLevel;
    private long saltLen;
    private long rsaLen;
    private byte[] serverSalt;
    private long encrType;

    private GBWRDPPublicKeyInfo publicKeyInfo;
    private GBWRDPKeySignatureInfo keySignatureInfo;

    public GBWRDPServerEncryptInfo(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        tag = buffer.readUInt16();
        len = buffer.readUInt16();
        rc4KeySize = buffer.readUInt32();
        encrLevel = buffer.readUInt32();
        saltLen = buffer.readUInt32();
        rsaLen = buffer.readUInt32();

        serverSalt = new byte[32];
        buffer.readRawBytes(serverSalt);

        encrType = buffer.readUInt32();
        /*unkown*/
        buffer.skip(8);

        publicKeyInfo = new GBWRDPPublicKeyInfo(buffer);
        keySignatureInfo = new GBWRDPKeySignatureInfo(buffer);
    }

    public GBWRDPKeySignatureInfo getKeySignatureInfo() {
        return keySignatureInfo;
    }

    public long getEncrLevel() {
        return encrLevel;
    }

    public long getEncrType() {
        return encrType;
    }

    public long getRc4KeySize() {
        return rc4KeySize;
    }

    public long getRsaLen() {
        return rsaLen;
    }

    public long getSaltLen() {
        return saltLen;
    }

    public int getTag() {
        return tag;
    }

    public int getLen() {
        return len;
    }

    public byte[] getServerSalt() {
        return serverSalt;
    }

    public GBWRDPPublicKeyInfo getPublicKeyInfo() {
        return publicKeyInfo;
    }
}
