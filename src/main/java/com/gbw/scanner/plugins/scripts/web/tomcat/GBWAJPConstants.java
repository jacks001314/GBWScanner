package com.gbw.scanner.plugins.scripts.web.tomcat;

public class GBWAJPConstants {

    public static final int OPTIONS = 1;
    public static final int GET = 2;
    public static final int HEAD =3;
    public static final int POST =4;
    public static final int PUT = 5;
    public static final int DELETE = 6;
    public static final int TRACE = 7;
    public static final int PROPFIND = 8;
    public static final int PROPPATCH = 9;
    public static final int MKCOL = 10;
    public static final int COPY = 11;
    public static final int MOVE = 12;
    public static final int LOCK = 13;
    public static final int UNLOCK = 14;
    public static final int ACL = 15;
    public static final int REPORT = 16;
    public static final int VERSION_CONTROL=17;
    public static final int CHECKIN = 18;
    public static final int CHECKOUT = 19;
    public static final int UNCHECKOUT = 20;
    public static final int SEARCH = 21;
    public static final int MKWORKSPACE = 22;
    public static final int UPDATE = 23;
    public static final int LABEL = 24;
    public static final int MERGE = 25;
    public static final int BASELINE_CONTROL=26;
    public static final int MKACTIVITY = 27;

    public static final int SC_REQ_ACCEPT = 1;
    public static final int SC_REQ_ACCEPT_CHARSET = 2;
    public static final int SC_REQ_ACCEPT_ENCODING = 3;
    public static final int SC_REQ_ACCEPT_LANGUAGE = 4;
    public static final int SC_REQ_AUTHORIZATION = 5;
    public static final int SC_REQ_CONNECTION = 6;
    public static final int SC_REQ_CONTENT_TYPE = 7;
    public static final int SC_REQ_CONTENT_LENGTH = 8;
    public static final int SC_REQ_COOKIE = 9;
    public static final int SC_REQ_COOKIE2 = 10;
    public static final int SC_REQ_HOST = 11;
    public static final int SC_REQ_PRAGMA = 12;
    public static final int SC_REQ_REFERER = 13;
    public static final int SC_REQ_USER_AGENT = 14;

    public static final int SERVER_TO_CONTAINER = 0;
    public static final int CONTAINER_TO_SERVER = 1;

    public static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.74 Safari/537.36";

    public static final String[] headerTransArray = new String[]{"accept", "accept-charset", "accept-encoding", "accept-language", "authorization", "connection", "content-type", "content-length", "cookie", "cookie2", "host", "pragma", "referer", "user-agent"};

    public static final String[] methodTransArray = new String[]{"OPTIONS", "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "PROPFIND", "PROPPATCH", "MKCOL", "COPY", "MOVE", "LOCK", "UNLOCK", "ACL", "REPORT", "VERSION-CONTROL", "CHECKIN", "CHECKOUT", "UNCHECKOUT", "SEARCH", "MKWORKSPACE", "UPDATE", "LABEL", "MERGE", "BASELINE-CONTROL", "MKACTIVITY"};

    public static final String[] attrTransArray = {"context", "servlet_path", "remote_user", "auth_type", "query_string", "route", "ssl_cert", "ssl_cipher", "ssl_session", "req_attribute", "ssl_key_size", "secret", "stored_method"};


    public static final int getMethodCode(String name){

        for(int i = 0;i<methodTransArray.length;i++){

            if(methodTransArray[i].equals(name.toUpperCase()))
                return i+1;
        }

        return -1;
    }

    public static final String getMethodString(int code){

        if(code<1||code>27)
            return null;

        return methodTransArray[code-1];
    }

    public static final int getHeaderCode(String name){

        for(int i = 0;i<headerTransArray.length;i++){

            if(headerTransArray[i].equals(name.toLowerCase()))
                return i+1;
        }

        return -1;
    }

    public static final String getHeaderString(int code){

        if(code<1||code>14)
            return null;

        return headerTransArray[code-1];
    }

    public static final int getAttrCode(String attr){

        for(int i = 0;i<attrTransArray.length;i++){

            if(attrTransArray[i].equals(attr.toLowerCase()))
                return i+1;
        }

        return -1;
    }

}
