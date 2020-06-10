package com.gbw.scanner.http;

import com.gbw.scanner.rule.GBWRuleConstants;
import com.gbw.scanner.rule.GBWRuleItem;
import com.gbw.scanner.rule.GBWRuleSourceEntry;
import com.gbw.scanner.utils.ByteDataUtils;
import com.xmap.api.utils.TextUtils;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

public class GBWHttpResponse implements GBWRuleSourceEntry {

    private int status;
    private String content;
    private byte[] bytes;
    private Map<String,String> headerMap;

    public GBWHttpResponse(){

        status = 0;
        content = "";
        bytes = null;
        headerMap = new HashMap<>();
    }

    public void setHeaders(Header[] headers){

        if(headers!=null){

            for(Header header:headers){

                headerMap.put(header.getName(),header.getValue());
            }
        }
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public boolean canMatch(String proto) {
        return true;
    }

    @Override
    public String getTargetValue(GBWRuleItem item) {

        String target = item.getTarget();
        boolean isHex = item.isBin();

        if(target.equals(GBWRuleConstants.status))
            return String.format("%d",status);

        if(target.equals(GBWRuleConstants.content)){

            if(bytes == null && TextUtils.isEmpty(content))
                return "";

            if(isHex){

                return ByteDataUtils.toHex(bytes==null?content.getBytes():bytes);
            }

            return content == null?new String(bytes):content;
        }

        if(target.startsWith(GBWRuleConstants.header)){

            String name = target.split("\\.")[1];

            return headerMap.get(name);
        }

        return null;
    }
}
