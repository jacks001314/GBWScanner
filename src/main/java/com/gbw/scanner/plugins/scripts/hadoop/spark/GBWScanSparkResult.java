package com.gbw.scanner.plugins.scripts.hadoop.spark;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWScanSparkResult  extends GBWScanScriptResult {

    private String scanType;
    private GBWScanSparkResponse response;

    public GBWScanSparkResult(GBWScanScriptCommonConfig scanScriptConfig, Host host,String scanType,GBWScanSparkResponse response) {
        super(scanScriptConfig, host, GBWScanScriptPlugin.sparkScan);

        this.response = response;
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("scanType",scanType);
        XContentBuilder cbb = cb.startObject("response");
        response.toJson(cbb);
        cbb.endObject();

        return cb;
    }


    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public void setResponse(GBWScanSparkResponse response) {
        this.response = response;
    }
}
