package com.gbw.scanner.plugins.scripts.fastjson;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWFastJsonScanResult  extends GBWScanScriptResult {


    private String dnsDomain;

    public GBWFastJsonScanResult(GBWScanScriptCommonConfig scanScriptConfig, Host host) {
        super(scanScriptConfig, host, GBWScanScriptPlugin.fastjsonScan);
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {
        super.makeDetails(cb);

        cb.field("dnsDomain",dnsDomain);

        return cb;
    }

    public String toString(){

        return String.format("Find OK:Fastjson RCE bug,address:%s:%d,dnsDomain:%s",getHost(),
                getPort(),dnsDomain);
    }



    public void setDnsDomain(String dnsDomain) {
        this.dnsDomain = dnsDomain;
    }


}
