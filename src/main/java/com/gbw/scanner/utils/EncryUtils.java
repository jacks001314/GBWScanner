package com.gbw.scanner.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryUtils {

    private EncryUtils(){

    }

    public static BigInteger makeRSAEncry(BigInteger value, BigInteger rsexp, BigInteger rsmod){

        return value.modPow(rsexp,rsmod);
    }

    public static String makeRDPHash48(byte[] sBytes, byte[] iBytes,
                                       byte[] clientRandomBytes, byte[] serverRandomBytes) throws NoSuchAlgorithmException {

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        sha1.update(iBytes);
        sha1.update(sBytes);
        sha1.update(clientRandomBytes);
        sha1.update(serverRandomBytes);

        md5.update(sBytes);
        md5.update(sha1.digest());

        return ByteDataUtils.toHex(md5.digest());
    }

    public static String makeRDPFinalHash(byte[] k, byte[] clientRandomBytes, byte[] serverRandomBytes) throws NoSuchAlgorithmException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(k);
        md5.update(clientRandomBytes);
        md5.update(serverRandomBytes);

        return ByteDataUtils.toHex(md5.digest());
    }

    public static String makeRDPHMac(byte[] macSaltKey, byte[] dataContent) throws NoSuchAlgorithmException {

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        byte[] pad1 = ByteDataUtils.parseHex("36",40);
        byte[] pad2 = ByteDataUtils.parseHex("5c",48);


        sha1.update(macSaltKey);
        sha1.update(pad1);
        sha1.update(ByteDataUtils.parseHex(ByteDataUtils.toHex(dataContent.length,4,true)));
        sha1.update(dataContent);

        md5.update(macSaltKey);
        md5.update(pad2);
        md5.update(sha1.digest());

        return ByteDataUtils.toHex(md5.digest());
    }


}
