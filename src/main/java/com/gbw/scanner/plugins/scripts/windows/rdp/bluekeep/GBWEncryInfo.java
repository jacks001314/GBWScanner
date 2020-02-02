package com.gbw.scanner.plugins.scripts.windows.rdp.bluekeep;

import com.gbw.scanner.protocol.rdp.GBWRDPMCSResponse;
import com.gbw.scanner.protocol.rdp.GBWRDPPublicKeyInfo;
import com.gbw.scanner.protocol.rdp.GBWRDPServerEncryptInfo;
import com.gbw.scanner.utils.ByteDataUtils;

import java.math.BigInteger;

public class GBWEncryInfo {

    private final BigInteger rcran;

    private final BigInteger rsexp;
    private final BigInteger rsmod;
    private final BigInteger rsran;
    private final byte[] rssalt;
    private final byte[] rcranData;

    private final long bitlen;

    public GBWEncryInfo(GBWRDPMCSResponse response){

        GBWRDPServerEncryptInfo encryptInfo = response.getUserData().getServerEncryptInfo();
        GBWRDPPublicKeyInfo publicKeyInfo = encryptInfo.getPublicKeyInfo();

        rcranData = ByteDataUtils.parseHex("41",32);

        rcran = ByteDataUtils.toBigInt("41",32,true,true);

        rsmod = ByteDataUtils.toBigInt(publicKeyInfo.getModulus(),true,true);
        rsexp = ByteDataUtils.toBigInt(publicKeyInfo.getExponent(),true,true);
        rsran = ByteDataUtils.toBigInt(encryptInfo.getServerSalt(),true,true);
        rssalt = encryptInfo.getServerSalt();
        bitlen = publicKeyInfo.getModulusLen();


    }

    public byte[] getRcranData() {
        return rcranData;
    }

    public BigInteger getRcran() {
        return rcran;
    }

    public BigInteger getRsexp() {
        return rsexp;
    }

    public BigInteger getRsmod() {
        return rsmod;
    }

    public BigInteger getRsran() {
        return rsran;
    }

    public byte[] getRssalt() {
        return rssalt;
    }

    public long getBitlen() {
        return bitlen;
    }

}
