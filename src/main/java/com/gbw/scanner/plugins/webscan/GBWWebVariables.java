package com.gbw.scanner.plugins.webscan;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GBWWebVariables {

    private GBWWebVariables(){

    }

    public static final String getString(HttpResponse response,String target){

        if(target.equals("status")){

            return Integer.toString(response.statusCode());
        }else if(target.equals("version")){

            return response.version().name();
        }else if(target.startsWith("header")){

            String name = target.split("\\.")[1];
            List<String> vars = response.headers().map().get(name);

            if(vars == null||vars.isEmpty())
                return null;

            return vars.get(0);
        }else if(target.equals("text")){

            return (String) response.body();
        }

        return null;
    }

    public static byte[] getBody(HttpResponse response){

        return (byte[]) response.body();
    }

    public static void main(String[] args){

        List<String> a = new ArrayList<>();
        a.add("shage");
        a.add("aaaa");

        String vv = a.toString();

        System.out.println(vv);
    }
}
