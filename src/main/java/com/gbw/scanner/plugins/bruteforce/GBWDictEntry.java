package com.gbw.scanner.plugins.bruteforce;

import com.xmap.api.utils.TextUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWDictEntry {

    private final String user;
    private final String passwd;

    public GBWDictEntry(String user,String passwd){

        this.user = user;
        this.passwd = passwd;
    }

    public String getUser() {
        return user;
    }

    public String getPasswd() {
        return passwd;
    }

    public String toString(){
        return String.format("%s|%s",user,passwd);
    }

    public void toJson(XContentBuilder cb) throws IOException {

        XContentBuilder cbb = cb.startObject();
        cbb.field("user",user);
        cbb.field("passwd",passwd);
        cbb.endObject();
    }
}
