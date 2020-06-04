package com.gbw.scanner.utils;

public class RuleUtils {

    public static String getRulePath(String root,String type){

        return String.format("%s/%s/%s.json",root,type,type);
    }

}
