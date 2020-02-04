package com.gbw.scanner.plugins.webscan;

import com.gbw.scanner.http.GBWHttpResponse;

public class GBWWebVariables {

    private GBWWebVariables(){

    }

    public static final String getString(GBWHttpResponse response, String target){

        if(target.equals("status")){

            return Integer.toString(response.getStatus());
        }else if(target.startsWith("header")){

            String name = target.split("\\.")[1];

            return response.getHeaderMap().get(name);

        }else if(target.equals("text")){

            return response.getContent();
        }

        return null;
    }

}
