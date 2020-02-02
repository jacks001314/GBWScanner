package com.gbw.scanner.protocol.rdp;

import com.gbw.scanner.protocol.GBWBuffer;

public class GBWRDPDomainParam {

    private int berTagDomainParams; /*1 byte*/
    private int berTagLen; /*1 byte*/


    private GBWRDPBerInfo channelsValue;

    private GBWRDPBerInfo usersValue;
    private GBWRDPBerInfo tokensValue;
    private GBWRDPBerInfo propertiesValue;
    private GBWRDPBerInfo throughoutValue;
    private GBWRDPBerInfo heightValue;
    private GBWRDPBerInfo pduMaxSize;
    private GBWRDPBerInfo protoVersion;

    public GBWRDPDomainParam(GBWBuffer<?> buffer) throws GBWBuffer.GBWBufferException {

        berTagDomainParams = buffer.readByte();
        berTagLen = buffer.readByte();

        channelsValue = new GBWRDPBerInfo(buffer);

        usersValue = new GBWRDPBerInfo(buffer);
        tokensValue = new GBWRDPBerInfo(buffer);
        propertiesValue = new GBWRDPBerInfo(buffer);
        throughoutValue = new GBWRDPBerInfo(buffer);
        heightValue = new GBWRDPBerInfo(buffer);
        pduMaxSize = new GBWRDPBerInfo(buffer,3);
        protoVersion = new GBWRDPBerInfo(buffer);
    }

    public GBWRDPBerInfo getProtoVersion() {
        return protoVersion;
    }

    public GBWRDPBerInfo getPduMaxSize() {
        return pduMaxSize;
    }

    public GBWRDPBerInfo getHeightValue() {
        return heightValue;
    }

    public GBWRDPBerInfo getThroughoutValue() {
        return throughoutValue;
    }

    public GBWRDPBerInfo getPropertiesValue() {
        return propertiesValue;
    }

    public GBWRDPBerInfo getTokensValue() {
        return tokensValue;
    }

    public GBWRDPBerInfo getUsersValue() {
        return usersValue;
    }

    public GBWRDPBerInfo getChannelsValue() {
        return channelsValue;
    }

    public int getBerTagLen() {
        return berTagLen;
    }

    public int getBerTagDomainParams() {
        return berTagDomainParams;
    }

}
