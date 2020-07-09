package com.gbw.scanner.utils;


import java.nio.charset.Charset;
import java.util.Base64;

public class Base64Utils {

    public static String encode(String content){

        return Base64.getEncoder().encodeToString(content.getBytes());
    }

    public static String encode(byte[] content){

        return Base64.getEncoder().encodeToString(content);
    }


    public static String decode(String content){

        return new String(Base64.getDecoder().decode(content), Charset.forName("utf-8"));
    }


}
