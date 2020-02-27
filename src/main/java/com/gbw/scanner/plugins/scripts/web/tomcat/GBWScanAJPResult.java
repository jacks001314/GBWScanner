package com.gbw.scanner.plugins.scripts.web.tomcat;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import com.gbw.scanner.utils.FileUtils;
import com.xmap.api.utils.IPUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GBWScanAJPResult extends GBWScanScriptResult {

    private GBWScanAJPConfig config;
    private String payload;

    public GBWScanAJPResult(GBWScanScriptCommonConfig scanScriptConfig, Host host, List<GBWAJPResponse> responses) {
        super(scanScriptConfig, host, GBWScanScriptPlugin.tomcatAJPScan);

        this.config = (GBWScanAJPConfig) scanScriptConfig;

        this.payload = storePayload(responses);
    }

    private String storePayload(List<GBWAJPResponse> responses)  {

        String fname = String.format("%s/%s_%s",config.getStoreDir(),config.getFilePrefix(), IPUtils.ipv4LongBE(getDstIP()));

        BufferedOutputStream writer = null;
        try {
            FileUtils.createPath(fname);
            writer = new BufferedOutputStream(new FileOutputStream(fname));
            for(GBWAJPResponse response:responses){

                writer.write(response.getData());
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(writer!=null){

                try {
                    writer.close();
                } catch (IOException e) {

                }
            }
        }

        return fname;
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("payload", payload);

        return cb;
    }

}
