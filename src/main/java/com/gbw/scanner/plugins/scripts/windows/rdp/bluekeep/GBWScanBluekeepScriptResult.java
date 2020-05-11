package com.gbw.scanner.plugins.scripts.windows.rdp.bluekeep;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWScanBluekeepScriptResult extends GBWScanScriptResult {

    private String payload;

    public GBWScanBluekeepScriptResult(GBWScanScriptCommonConfig scanScriptConfig, Host host) {

        super(scanScriptConfig, host, GBWScanScriptPlugin.bluekeepScan);

    }


    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        super.makeDetails(cb);
        cb.field("payload",payload);
        return cb;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}


