package com.gbw.scanner.rule;

public class GBWRuleConstants {

    private GBWRuleConstants(){

    }

    public static final String arrSplit = ",";

    /*rule op names*/
    public static final String contains = "contains";
    public static final String startsWith = "startsWith";
    public static final String endsWith = "endsWith";
    public static final String regex = "regex";
    public static final String eq = "eq";
    public static final String lt = "lt";
    public static final String gt = "gt";
    public static final String le = "le";
    public static final String ge = "ge";
    public static final String in = "in";


    /*target name for http session*/
    public static final String status = "status";
    public static final String content = "content";
    public static final String header = "header";

}
