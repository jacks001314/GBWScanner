package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import com.gbw.scanner.utils.FileUtils;
import com.xmap.api.utils.IPUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWScanAJPResult extends GBWScanScriptResult {

    private GBWScanAJPConfig config;
    private String payload;

    public GBWScanAJPResult(GBWScanScriptCommonConfig scanScriptConfig, Host host, byte[] data) {
        super(scanScriptConfig, host, GBWScanScriptPlugin.tomcatAJPScan);

        this.config = (GBWScanAJPConfig) scanScriptConfig;

        this.payload = storePayload(data);
    }

    private String storePayload(byte[] data)  {

        String fname = String.format("%s/%s_%s",config.getStoreDir(),config.getFilePrefix(), IPUtils.ipv4LongBE(getDstIP()));

        try {
            FileUtils.write(fname,data);
        }catch (IOException e){
            e.printStackTrace();
        }

        return fname;
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("payload", payload);

        return cb;
    }

}
