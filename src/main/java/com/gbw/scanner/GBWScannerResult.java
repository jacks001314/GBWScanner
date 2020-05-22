package com.gbw.scanner;

import com.gbw.scanner.sink.es.ESIndexable;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;


public abstract class GBWScannerResult implements ESIndexable {

    private long time;

    private String proto;
    private String ip;
    private String host;
    private int port;

    private String scanType;

    private String type;
    private String desc;
    private String code;
    private String subject;

    @Override
    public XContentBuilder dataToJson(XContentBuilder cb) throws IOException {

        cb.field("time",time);
        cb.field("proto",proto == null?"":proto);
        cb.field("ip",ip);
        cb.field("host", TextUtils.isEmpty(host)?ip:host);
        cb.field("port",port);
        cb.field("scanType",scanType);

        cb.field("type",type);
        cb.field("desc",desc);
        cb.field("code",code);
        cb.field("subject",subject);

        XContentBuilder details = cb.startObject("details");
        makeDetails(details);
        details.endObject();

        return cb;
    }

    public abstract XContentBuilder makeDetails(XContentBuilder cb) throws IOException;

    public String getDstIP(){

        return ip;
    }

    public String getAssetsIP(){

        return ip;
    }
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getScanType() {
        return scanType;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }
}
