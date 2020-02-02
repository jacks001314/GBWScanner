package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;
import com.gbw.scanner.utils.ByteDataUtils;

public class GBWRDPMCSResponse {

    private GBWRDPHeader header;

    private int plen; /*1 byte*/
    private int ptype; /*1 byte*/
    private int eot; /*1 byte*/
    private int mcsTag; /*2 byte*/
    private int mcsLen; /*3 bytes */

    private GBWRDPBerInfo resultValue;
    private GBWRDPBerInfo connectId;

    /*Domain params*/
    private GBWRDPDomainParam domainParam;

    private GBWRDPUserData userData;


    public GBWRDPMCSResponse(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        header = new GBWRDPHeader(buffer);
        plen = buffer.readByte();
        ptype = buffer.readByte();
        eot = buffer.readByte();
        mcsTag = buffer.readUInt16();
        mcsLen = ByteDataUtils.toBigInt(buffer.readRawBytes(3),true,false).intValue();

        resultValue = new GBWRDPBerInfo(buffer);
        connectId = new GBWRDPBerInfo(buffer);

        domainParam = new GBWRDPDomainParam(buffer);

        userData =  new GBWRDPUserData(buffer);
    }

    public GBWRDPDomainParam getDomainParam() {
        return domainParam;
    }

    public GBWRDPUserData getUserData() {
        return userData;
    }


    public int getMcsTag() {
        return mcsTag;
    }

    public int getEot() {
        return eot;
    }

    public GBWRDPHeader getHeader() {
        return header;
    }

    public int getPlen() {
        return plen;
    }

    public int getPtype() {
        return ptype;
    }

    public int getMcsLen() {
        return mcsLen;
    }


    public GBWRDPBerInfo getResultValue() {
        return resultValue;
    }


    public GBWRDPBerInfo getConnectId() {
        return connectId;
    }
}
