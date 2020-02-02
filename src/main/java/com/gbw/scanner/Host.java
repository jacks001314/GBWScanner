package com.gbw.scanner;

import com.xmap.api.utils.IPUtils;
import com.xmap.api.utils.TextUtils;

import java.util.List;

public class Host {

    private String host;
    private String ip;
    private int port;
    private List<String> types;
    private String proto;

    public Host(String host,String ip,int port,List<String> types,String proto){

        this.host = host;
        this.ip = ip;
        this.port = port;
        this.types = types;
        this.proto = proto;
    }

    public Host(String host,long ip,int port,List<String> types,String proto){

        this.host = host;
        this.ip = IPUtils.ipv4Str(ip);
        this.port = port;
        this.types = types;
        this.proto = proto;
    }

    @Override
    public boolean equals(Object obj) {

        Host other = (Host)obj;

        if(!TextUtils.isEmpty(host)){

            if(TextUtils.isEmpty(other.getHost())||(!host.equals(other.getHost())))
                return false;
        }else{

            if(!TextUtils.isEmpty(other.getHost()))
                return false;
        }

        return ip.equals(other.getIp())&&port==other.getPort();

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServer(){
        if(TextUtils.isEmpty(host))
            return ip;
        else
            return host;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
