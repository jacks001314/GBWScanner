package com.gbw.scanner.plugins.scripts.windows.rdp.bluekeep;

import com.gbw.scanner.protocol.GBWEndian;
import com.gbw.scanner.protocol.GBWProtoBuffer;
import com.gbw.scanner.utils.ByteDataUtils;
import com.gbw.scanner.utils.EncryUtils;

import java.math.BigInteger;

public class GBWRDPRequestBuilder {

    private GBWRDPRequestBuilder(){

    }

    public static final byte[] makeConnectRequest(String userName){

        GBWProtoBuffer rdpBuffer = new GBWProtoBuffer();
        rdpBuffer.putRawBytes(ByteDataUtils.parseHex("030000")); /*TPKT Header*/
        rdpBuffer.putByte((byte)(33+5+userName.length())); /*TPKT total length*/
        rdpBuffer.putByte((byte)(33+userName.length())); /*Length indicator*/
        rdpBuffer.putByte((byte)0xe0); /*ype - TPDU*/
        rdpBuffer.putUInt16(0x0); /*Destination reference*/
        rdpBuffer.putUInt16(0x00); /*Source reference*/
        rdpBuffer.putByte((byte)0x0); /*Class and options*/
        rdpBuffer.putRawBytes("Cookie: mstshash=".getBytes()); /*Cookie: mstshash=*/
        rdpBuffer.putRawBytes(userName.getBytes());
        rdpBuffer.putRawBytes(ByteDataUtils.parseHex("0d0a")); /*Cookie terminator sequence*/
        rdpBuffer.putByte((byte)0x01); /*ype: RDP_NEG_REQ)*/
        rdpBuffer.putByte((byte)0x0); /*RDP_NEG_REQ::flags*/
        rdpBuffer.putRawBytes(ByteDataUtils.parseHex("0800")); /*RDP_NEG_REQ::length (8 bytes)*/
        rdpBuffer.putUInt32(0x0); /* Requested protocols (PROTOCOL_RDP)*/

        return rdpBuffer.getCompactData();
    }

    private static final String append(String raw,String sub,int n){

        StringBuffer sb = new StringBuffer(raw);

        for(int i = 0;i<n;i++){

            sb.append(sub);
        }

        return sb.toString();
    }

    public static final byte[] makeMCSGCCRequest(String hostname) {

        String raw = ByteDataUtils.toHex(hostname);
        String hostnameHex = append(raw, "00", 32 - hostname.length());

        String gccRequest = "030001ca" +    // TPKT Header
                "02f080" +             // x.224
                "7f658201be" + // change here
                "04010104" +
                "01010101ff" +
                "30200202002202020002020200000202000102020000020200010202ffff020200023020" +
                "020200010202000102020001020200010202000002020001020204200202000230200202" +
                "ffff0202fc170202ffff0202000102020000020200010202ffff020200020482014b" + // chnage here
                "000500147c00018142" + // change here - ConnectPDU
                "000800100001c000447563618134" + // chnage here
                "01c0d800040008002003580201ca03aa09040000280a0000";

        gccRequest += hostnameHex; // Client name -32 Bytes - we45-lt35

        gccRequest +=
                "04000000000000000c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001ca0100000000001800070001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000004c00c00090000000000000002c00c000300000000000000" +
                        "03c0" +
                        "4400" +
                        "04000000" + //channel count
                        "636c697072647200c0a00000" + //cliprdr
                        "4d535f543132300000000000" + //MS_T120
                        "726470736e640000c0000000" + //rdpsnd
                        "736e646462670000c0000000" + //snddbg
                        "726470647200000080800000"; //rdpdr

        return ByteDataUtils.parseHex(gccRequest);
    }

    public static final byte[] makeMCSErectDomainRequest(){

        String hex  = "0300000c02f0800400010001";

        return ByteDataUtils.parseHex(hex);

    }

    public static final byte[] makeMSCAttachUserRequest(){

        String hex = "0300000802f08028";
        return ByteDataUtils.parseHex(hex);
    }

    public static final byte[] makeChannelRequest(int userId,int chId){

        String hex  = "0300000c02f08038";

        GBWProtoBuffer buffer = new GBWProtoBuffer(GBWEndian.BE);

        buffer.putUInt16(userId);
        buffer.putUInt16(chId);

        hex += ByteDataUtils.toHex(buffer.getCompactData());

        return ByteDataUtils.parseHex(hex);
    }

    public static final byte[] makeSecurityExchangeRequest(GBWEncryInfo encryInfo){

        BigInteger encryptRCRan = EncryUtils.makeRSAEncry(encryInfo.getRcran(),encryInfo.getRsexp(),encryInfo.getRsmod());
        String encryptRCRanHex = ByteDataUtils.toHex(encryptRCRan,true);
        String bitlenHex = ByteDataUtils.toHex(encryInfo.getBitlen()+8,4,true);

        long userdata_length = encryInfo.getBitlen()+16;
        long userdata_length_low = userdata_length & 0xFF;
        long userdata_length_high = userdata_length / 256;

        long flags = 0x80 | userdata_length_high;

        String pkt = "0300";
        pkt+=ByteDataUtils.toHex(userdata_length+15,2,false); // TPKT
        pkt+="02f080"; //X.224
        pkt+="64" ;// sendDataRequest
        pkt+="0008"; //intiator userId
        pkt+="03eb"; // channelId = 1003
        pkt+="70"; // dataPriority
        pkt+=ByteDataUtils.toHex(flags,1,true);
        pkt+=ByteDataUtils.toHex(userdata_length_low,1,true);  // UserData length
        pkt+="0100"; // securityHeader flags
        pkt+="0000"; //securityHeader flagsHi
        pkt+= bitlenHex; //  securityPkt length
        pkt+= encryptRCRanHex; // # 64 bytes encrypted client random
        pkt+= "0000000000000000"; //  8 bytes rear padding (always present)*/

        return ByteDataUtils.parseHex(pkt);
    }

    public static final String makeClientData(){

        String data = "000000003301000000000a000000000000000000";
        data+="75007300650072003000"; // FIXME: username
        data+="000000000000000002001c00";
        data+="3100390032002e003100360038002e0031002e00320030003800";// # FIXME: ip
        data+="00003c0043003a005c00570049004e004e0054005c00530079007300740065006d00330032005c" +
                "006d007300740073006300610078002e0064006c006c000000a40100004700540042002c0020006e006f0072006d00" +
                "61006c0074006900640000000000000000000000000000000000000000000000000000000000000000000000000000000a0000" +
                "0005000300000000000000000000004700540042002c00200073006f006d006d006100720074006900640000000000000000000" +
                "000000000000000000000000000000000000000000000000000000000000300000005000200000000000000c4ffffff00000000270000000000";


        return data;
    }

    public static final String makeClientConfirmActiveData(){

        String data = "a4011300f103ea030100ea0306008e014d53545343000e00000001001800010003000002000000000d04000000000000000002" +
                "001c00100001000100010020035802000001000100000001000000030058000000000000000000000000000000000000000000010014000000010047" +
                "012a000101010100000000010101010001010000000000010101000001010100000000a1060000000000000084030000000000e404000013002800000000037800" +
                "00007800000050010000000000000000000000000000000000000000000008000a000100140014000a0008000600000007000c00000000000000000005000c000000000002000" +
                "20009000800000000000f000800010000000d005800010000000904000004000000000000000c00000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000000c000800010000000e0008000100000010003400fe000400fe000400fe000800fe000800fe0010" +
                "00fe002000fe004000fe008000fe000001400000080001000102000000";

        return data;
    }

    public static final String makeClientSynData(){

        String data = "16001700f103ea030100000108001f0000000100ea03";
        return data;
    }

    public static final String makeClientControlCooperateData(){
        String data = "1a001700f103ea03010000010c00140000000400000000000000";
        return data;
    }

    public static final String makeClientControlAndControlData(){

        String data = "1a001700f103ea03010000010c00140000000100000000000000";
        return data;
    }

    public static final String makeClientPersistentKeylistData(){

        String data = "49031700f103ea03010000013b031c00000001000000000000000000000000000000" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        return data;
    }

    public static String makeClientFontlistData(){

        String data = "1a001700f103ea03010000010c00270000000000000003003200";
        return data;
    }

    public static byte[] makeEncrypteRequest(String hexData,GBWRC4Key rc4Key,String flagsHex,String flagsHiHex,String channelIdHex) throws Exception {

        byte[] content = ByteDataUtils.parseHex(hexData);

        int userDataLen = content.length + 12;

        int udlWithFlag = 0x8000 | userDataLen;
        String pkt = "02f080"; //  X.224
        pkt+= "64"; // sendDataRequest
        pkt+= "0008";//  intiator userId .. TODO: for a functional client this isn't static
        pkt+= channelIdHex; // channelId = 1003
        pkt+= "70"; //dataPriority
        pkt+= ByteDataUtils.toHex(udlWithFlag,2,false);
        pkt+= flagsHex; //4800  flags  SEC_INFO_PKT | SEC_ENCRYPT
        pkt+= flagsHiHex;// flagsHi

        pkt+= EncryUtils.makeRDPHMac(ByteDataUtils.parseHex(rc4Key.getMacKey()),content).substring(0,16);

        pkt+= ByteDataUtils.toHex(rc4Key.getRc4().crypt(content));

        String tpkt = "0300";
        tpkt+= ByteDataUtils.toHex((pkt.length()/2 + 4),2,false);
        tpkt+=pkt;


        return ByteDataUtils.parseHex(tpkt);

    }


}
