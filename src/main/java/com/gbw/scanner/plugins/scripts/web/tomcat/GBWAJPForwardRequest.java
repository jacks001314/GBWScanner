package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.Host;

import java.util.ArrayList;
import java.util.List;

public class GBWAJPForwardRequest {

    private int prefix_code;
    private int method;
    private String protocol;
    private String req_uri;
    private String remote_addr;
    private String remote_host;
    private String server_name;
    private int server_port;
    private boolean is_ssl;

    private List<GBWRecord> request_headers;
    private List<GBWRecord> attributes;

    private int data_direction;

    public GBWAJPForwardRequest(Host host, String uri){

        prefix_code = 0x02;
        method = GBWAJPConstants.GET;
        protocol = "HTTP/1.1";
        req_uri = uri;
        remote_addr = host.getServer();
        remote_host = host.getHost();
        server_name = host.getServer();
        server_port = host.getPort();

        request_headers = new ArrayList<>();

        addHeader(GBWAJPConstants.getHeaderString(GBWAJPConstants.SC_REQ_HOST),host.getServer());
        addHeader(GBWAJPConstants.getHeaderString(GBWAJPConstants.SC_REQ_USER_AGENT),GBWAJPConstants.UA);
        attributes = new ArrayList<>();

        is_ssl = false;

    }

    private void packHeaders(GBWAJPMessage message){

        message.appendInt(request_headers.size());
        int code;
        for(GBWRecord header:request_headers){

            code = GBWAJPConstants.getHeaderCode(header.getName());
            if(code !=-1){

                message.appendByte(0xA0);
                message.appendByte(code);
            }else{
                message.appendString(header.getName());
            }
            message.appendString(header.getValue());
        }
    }

    private void packAttributes(GBWAJPMessage message){

        int code;
        for(GBWRecord attr:attributes){

            code = GBWAJPConstants.getAttrCode(attr.getName());
            message.appendByte(code);

            if(attr.getName().equals("req_attribute")){
                String kv[] = attr.getValue().split(",");
                message.appendString(kv[0]);
                message.appendString(kv[1]);
            }else{
                message.appendString(attr.getValue());
            }
        }

    }

    public void end(GBWAJPMessage message) {

        int len = message.getPos();
        byte[] buf = message.getBuffer();
        int dLen = len - 4;

        if(data_direction == GBWAJPConstants.SERVER_TO_CONTAINER){
            buf[0] = (byte) 0x12;
            buf[1] = (byte) 0x34;
        }else{
            buf[0] = (byte) 0x41;
            buf[1] = (byte) 0x42;
        }

        buf[2] = (byte) ((dLen>>>8) & 0xFF);
        buf[3] = (byte) (dLen & 0xFF);
    }

    public GBWAJPMessage createMessage(int packetSize){

        GBWAJPMessage message = new GBWAJPMessage(packetSize);
        message.reset();
        message.appendByte(prefix_code);
        message.appendByte(method);
        message.appendString(protocol);
        message.appendString(req_uri);
        message.appendString(remote_addr);
        message.appendString(remote_host);
        message.appendString(server_name);
        message.appendInt(server_port);
        message.appendByte(is_ssl?0x01:0x00);

        packHeaders(message);
        packAttributes(message);

        message.appendByte(0xFF);

        end(message);

        return message;
    }

    public void addHeader(String name,String value){

        request_headers.add(new GBWRecord(name,value));
    }

    public void addAttr(String name,String value){

        attributes.add(new GBWRecord(name,value));
    }

    public int getPrefix_code() {
        return prefix_code;
    }

    public void setPrefix_code(int prefix_code) {
        this.prefix_code = prefix_code;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getReq_uri() {
        return req_uri;
    }

    public void setReq_uri(String req_uri) {
        this.req_uri = req_uri;
    }

    public String getRemote_addr() {
        return remote_addr;
    }

    public void setRemote_addr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    public String getRemote_host() {
        return remote_host;
    }

    public void setRemote_host(String remote_host) {
        this.remote_host = remote_host;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public int getServer_port() {
        return server_port;
    }

    public void setServer_port(int server_port) {
        this.server_port = server_port;
    }

    public boolean isIs_ssl() {
        return is_ssl;
    }

    public void setIs_ssl(boolean is_ssl) {
        this.is_ssl = is_ssl;
    }

    public List<GBWRecord> getRequest_headers() {
        return request_headers;
    }

    public void setRequest_headers(List<GBWRecord> request_headers) {
        this.request_headers = request_headers;
    }

    public List<GBWRecord> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<GBWRecord> attributes) {
        this.attributes = attributes;
    }

    public void setData_direction(int data_direction) {
        this.data_direction = data_direction;
    }
}
