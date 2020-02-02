package com.gbw.scanner.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GBWOPUtils {

    private static final String exception = ":unknown.host 451";

    public static boolean isException(byte[] data){

        if(data == null ||data.length == 0)
            return true;

        if(GBWOPUtils.contains(data,exception.getBytes()))
            return true;

        return false;
    }

    public static boolean isException(String data){

        if(data == null ||data.length() == 0)
            return true;

        if(GBWOPUtils.contains(data,exception))
            return true;

        return false;
    }

    public static final boolean isMatch(String data,String op,String opData){

        if(op.equals("reg")){
            return reg(data,opData);
        }else if(op.equals("eq")){
            return eq(data,opData);
        }else if(op.equals("contains")){
            return contains(data,opData);
        }else if(op.equals("containsI")){
            return contains(data.toLowerCase(),opData.toLowerCase());
        }else if(op.equals("startsWith")){
            return startsWith(data,opData);
        }else if(op.equals("endsWith")){
            return endsWith(data,opData);
        }else{
            return false;
        }
    }

    public static final boolean isMatch(byte[] data,String op,String opData){

        byte[] byteOPData = ByteDataUtils.parseHex(opData);

        if(op.equals("eq")){
            return eq(data,byteOPData);
        }else if(op.equals("contains")){
            return contains(data,byteOPData);
        }else if(op.equals("startsWith")){

            return startsWith(data,byteOPData);
        }else if(op.equals("endsWith")){
            return endsWith(data,byteOPData);
        }else {
            return false;
        }
    }

    public static final boolean reg(String data,String req) {

        Pattern pattern = Pattern.compile(req);
        Matcher matcher = pattern.matcher(data);

        return matcher.matches();
    }

    public static final boolean reg(String data,Pattern pattern){

        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }

    public static final boolean eq(String str1,String str2){

        return str1.equals(str2);
    }

    public static final boolean eq(byte[] d1,byte[] d2){

        if(d1.length!=d2.length)
            return false;

        return ByteDataUtils.equals(d1,0,d2,0,d2.length);
    }

    public static final boolean contains(String str,String sub){

        return str.contains(sub);
    }

    public static final boolean contains(byte[] data,byte[] sub){

        return ByteDataUtils.contains(data,sub);
    }

    public static final boolean startsWith(String str,String sub){

        return str.startsWith(sub);
    }

    public static final boolean startsWith(byte[] data,byte[] sub){

        return ByteDataUtils.equals(data,0,sub,0,sub.length);
    }

    public static final boolean endsWith(String str,String sub){

        return str.endsWith(sub);
    }

    public static final boolean endsWith(byte[] data,byte[] sub){
        int dlen = data.length-sub.length;

        if(dlen<0)
            return false;

        return ByteDataUtils.equals(data,dlen,sub,0,sub.length);
    }

}
