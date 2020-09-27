package com.gbw.scanner.plugins.scripts.weblogic;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import com.gbw.scanner.plugins.scripts.weblogic.vuls.GBWVulCheckResult;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWScanWeblogicResult extends GBWScanScriptResult {

    private String version;
    private String cmd;
    private String result;
    private String payload;

    public GBWScanWeblogicResult(GBWScanScriptCommonConfig scanScriptConfig, Host host, GBWVulCheckResult scanResult) {
        super(scanScriptConfig, host, scanResult.getType());
        setCmd(scanResult.getCmd());
        setCode(scanResult.getCode());
        setResult(scanResult.getResult());

        setVersion(scanResult.getVersion());

    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        super.makeDetails(cb);
        cb.field("version", TextUtils.isEmpty(version)?"":version);
        cb.field("cmd", TextUtils.isEmpty(cmd)?"":cmd);
        cb.field("result", TextUtils.isEmpty(result)?"":result);
        cb.field("payload", TextUtils.isEmpty(payload)?"":payload);
        return cb;
    }

    public String toString(){

        return String.format("{version:%s,type:%s,code:%s,result:%s,payload:%s}",
                version,getType(),getCode(),result,payload);
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
