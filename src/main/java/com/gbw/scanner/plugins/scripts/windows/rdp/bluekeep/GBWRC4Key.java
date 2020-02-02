package com.gbw.scanner.plugins.scripts.windows.rdp.bluekeep;

import com.gbw.scanner.crypto.RC4;
import com.gbw.scanner.utils.ByteDataUtils;
import com.gbw.scanner.utils.EncryUtils;

public class GBWRC4Key {


    private String initialClientEncryptKey128;
    private String initialClientDecryptKey128;
    private String macKey;
    private String sessionKeyBlob;
    private RC4 rc4;

    public GBWRC4Key(GBWEncryInfo encryInfo) throws Exception {

        byte[] preMasterSecret = new byte[48];
        byte[] clientRandom = encryInfo.getRcranData();
        byte[] serverRandom = encryInfo.getRssalt();


        System.arraycopy(clientRandom,0,preMasterSecret,0,24);
        System.arraycopy(serverRandom,0,preMasterSecret,24,24);

        String masterSecretHex = EncryUtils.makeRDPHash48(preMasterSecret,"A".getBytes(),clientRandom,serverRandom)
                +EncryUtils.makeRDPHash48(preMasterSecret,"BB".getBytes(),clientRandom,serverRandom)
                +EncryUtils.makeRDPHash48(preMasterSecret,"CCC".getBytes(),clientRandom,serverRandom);

        byte[] masterSecret = ByteDataUtils.parseHex(masterSecretHex);

        String sessionKeyBlobHex = EncryUtils.makeRDPHash48(masterSecret,"X".getBytes(),clientRandom,serverRandom)
                +EncryUtils.makeRDPHash48(masterSecret,"YY".getBytes(),clientRandom,serverRandom)
                +EncryUtils.makeRDPHash48(masterSecret,"ZZZ".getBytes(),clientRandom,serverRandom);

        byte[] sessionKeyBlobBB = ByteDataUtils.parseHex(sessionKeyBlobHex);

        setInitialClientDecryptKey128(EncryUtils.makeRDPFinalHash(ByteDataUtils.copyof(sessionKeyBlobBB,16,32),clientRandom,serverRandom));
        setInitialClientEncryptKey128(EncryUtils.makeRDPFinalHash(ByteDataUtils.copyof(sessionKeyBlobBB,32,48),clientRandom,serverRandom));
        setMacKey(ByteDataUtils.toHex(ByteDataUtils.copyof(sessionKeyBlobBB,0,16)));
        setSessionKeyBlob(sessionKeyBlobHex);

        rc4 = new RC4();
        rc4.engineInitEncrypt(ByteDataUtils.parseHex(initialClientEncryptKey128));

    }


    public RC4 getRc4() {
        return rc4;
    }


    public String getInitialClientEncryptKey128() {
        return initialClientEncryptKey128;
    }

    public void setInitialClientEncryptKey128(String initialClientEncryptKey128) {
        this.initialClientEncryptKey128 = initialClientEncryptKey128;
    }

    public String getInitialClientDecryptKey128() {
        return initialClientDecryptKey128;
    }

    public void setInitialClientDecryptKey128(String initialClientDecryptKey128) {
        this.initialClientDecryptKey128 = initialClientDecryptKey128;
    }

    public String getMacKey() {
        return macKey;
    }

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }


    public String getSessionKeyBlob() {
        return sessionKeyBlob;
    }

    public void setSessionKeyBlob(String sessionKeyBlob) {
        this.sessionKeyBlob = sessionKeyBlob;
    }


}
