package com.gbw.scanner.plugins.scripts.web.flink;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWScanFlinkScriptResult extends GBWScanScriptResult {

    private String payload;

    public GBWScanFlinkScriptResult(GBWScanScriptCommonConfig scanScriptConfig, Host host) {
        super(scanScriptConfig, host, GBWScanScriptPlugin.flinkScan);
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        super.makeDetails(cb);
        cb.field("payload", TextUtils.isEmpty(payload)?"":payload);

        return cb;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
