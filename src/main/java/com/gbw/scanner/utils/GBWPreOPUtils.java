package com.gbw.scanner.utils;

import com.xmap.api.utils.Text;

import java.nio.charset.CharacterCodingException;
import java.util.Base64;

public class GBWPreOPUtils {


    public static final String toLower(String str){

        return str.toLowerCase();
    }

    public static final String toUpper(String str){

        return str.toUpperCase();
    }

    public static final String b64ToStr(String str){

        try {
            return Text.decode(Base64.getDecoder().decode(str));
        } catch (CharacterCodingException e) {
            return str;
        }
    }

    public static final byte[] b64ToByte(String str){

        return Base64.getDecoder().decode(str);
    }

    public static String toBase64(String str){

        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static String toBase64(byte[] data){

        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] hexToBytes(String data){

        return ByteDataUtils.parseHex(data);
    }

}
