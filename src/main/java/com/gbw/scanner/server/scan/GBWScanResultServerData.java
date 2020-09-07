package com.gbw.scanner.server.scan;

import com.xmap.api.utils.TextUtils;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

public class GBWScanResultServerData {

    private String type;
    private String proto;
    private String ip;
    private String payload;
    private int port;

    public GBWScanResultServerData(HttpRequest request){

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> params = queryStringDecoder.parameters();

        setType(getValue(params,"type"));
        setProto(getValue(params,"proto"));
        setIp(getValue(params,"ip"));
        setPayload(getValue(params,"payload"));
        String pstr = getValue(params,"port");
        setPort(TextUtils.isEmpty(pstr)?0:Integer.parseInt(pstr));


    }

    private String getValue(Map<String,List<String>> params,String key){

        if(params == null)
            return "";

        List<String> values = params.get(key);
        if(values == null||values.size()==0)
            return "";

        return values.get(0);
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
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

}
