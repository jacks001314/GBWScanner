package com.gbw.scanner.plugins.bruteforce;

import com.gbw.scanner.GBWScannerResult;
import com.gbw.scanner.Host;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GBWBruteForceResult extends GBWScannerResult {

    private GBWDictEntry entry;

    public GBWBruteForceResult(GBWDictEntry entry,Host host,String type){

        this.entry = entry;

        setTime(System.currentTimeMillis());
        setHost(host.getHost());
        setIp(host.getIp());
        setPort(host.getPort());
        setType(type);
    }

    public GBWDictEntry getEntry() {
        return entry;
    }

    public void setEntry(GBWDictEntry entry) {
        this.entry = entry;
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("count",1);
        XContentBuilder cbb = cb.startArray("passwds");
        cbb.value(entry.toString());
        cbb.endArray();

        return cb;
    }

    public String toString(){

        StringBuffer sb = new StringBuffer("BruteForce ok:\n");
        sb.append("{host:");
        sb.append(TextUtils.isEmpty(getHost())?getIp():getHost());
        sb.append(",port:");
        sb.append(getPort());
        sb.append(",user:");
        sb.append(entry.getUser());
        sb.append(",passwd:");
        sb.append(entry.getPasswd());
        sb.append("}\n");

        return sb.toString();
    }

    @Override
    public String getIndexMapping() {

        String mapping = "{" +
                "\"properties\":{" +
                "\"id\":{\"type\":\"keyword\"}," +
                "\"time\":{\"type\":\"long\"}," +
                "\"ip\":{\"type\":\"keyword\"}," +
                "\"port\":{\"type\":\"integer\"}," +
                "\"host\":{\"type\":\"keyword\"}," +
                "\"type\":{\"type\":\"keyword\"}," +
                "\"details\":{" +
                "\"properties\":{" +
                "\"passwds\":{\"type\":\"keyword\"}," +
                "\"count\":{\"type\":\"long\"}" +
                "}" +
                "}," +
                "\"srcIPLocation\":{" +
                "\"properties\":{" +
                "\"location\":{\"type\":\"keyword\"}," +
                "\"country\":{\"type\":\"keyword\"}," +
                "\"city\":{\"type\":\"keyword\"}," +
                "\"longitude\":{\"type\":\"double\"}," +
                "\"latitude\":{\"type\":\"double\"}" +
                "}" +
                "}," +
                "\"dstIPLocation\":{" +
                "\"properties\":{" +
                "\"location\":{\"type\":\"keyword\"}," +
                "\"country\":{\"type\":\"keyword\"}," +
                "\"city\":{\"type\":\"keyword\"}," +
                "\"longitude\":{\"type\":\"double\"}," +
                "\"latitude\":{\"type\":\"double\"}" +
                "}" +
                "}" +
                "}" +
                "}";

        return mapping;
    }

    @Override
    public String getIndexNamePrefix() {
        return "security_bug_bruteforce";
    }

    @Override
    public String getIndexDocType()
    {
        return "SecurityBugDocDB";
    }

    public Script getUpdateScript(){

        Map<String,Object> params = new HashMap<>();
        params.put("value",entry.toString());

        String code = " if(!ctx._source.details.passwds.contains(params.value))\n" +
                "            ctx._source.details.passwds.add(params.value);";


        return new Script(ScriptType.INLINE,"painless",code,params);
    }

    public String getId(){

        return String.format("%s:%d",getIp(),getPort());

    }

}
