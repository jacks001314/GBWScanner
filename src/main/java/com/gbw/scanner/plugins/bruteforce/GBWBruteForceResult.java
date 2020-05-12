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
    private String cmd;
    private String cmdResult;

    public GBWBruteForceResult(GBWDictEntry entry,Host host,String type){

        this.entry = entry;
        this.cmd = "";
        this.cmdResult = "";

        setTime(System.currentTimeMillis());
        setHost(host.getHost());
        setIp(host.getIp());
        setPort(host.getPort());
        setScanType(type);
        setType(type);
        setDesc(String.format("%s暴力破解",host.getProto()));
        setCode(type);
        setSubject("请不要使用弱口令，请添加登录次数限制，添加验证码机制");
    }

    public GBWDictEntry getEntry() {
        return entry;
    }

    public void setEntry(GBWDictEntry entry) {
        this.entry = entry;
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("cmd",cmd);
        cb.field("cmdResult",cmdResult);

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
        sb.append(",cmd:");
        sb.append(cmd);
        sb.append(",cmdResult:");
        sb.append(cmdResult);

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
                "\"scanType\":{\"type\":\"keyword\"}," +
                "\"code\":{\"type\":\"keyword\"}," +
                "\"type\":{\"type\":\"keyword\"},"+
                "\"desc\":{\"type\":\"keyword\"},"+
                "\"subject\":{\"type\":\"keyword\"},"+
                "\"details\":{" +
                "\"properties\":{" +
                "\"passwds\":{\"type\":\"keyword\"}," +
                "\"cmd\":{\"type\":\"keyword\"}," +
                "\"cmdResult\":{\"type\":\"keyword\"}," +
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

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getCmdResult() {
        return cmdResult;
    }

    public void setCmdResult(String cmdResult) {
        this.cmdResult = cmdResult;
    }
}
