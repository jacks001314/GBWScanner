package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWScanYarnResult  extends GBWScanScriptResult {

    private String appID;
    private int nodes;
    private long maxMem;
    private int maxVcores;
    private String scan;

    public GBWScanYarnResult(GBWScanScriptCommonConfig scanScriptConfig, Host host) {
        super(scanScriptConfig, host, GBWScanScriptPlugin.hadoopYarnScan);

        nodes = 0;
        maxMem = 0;
        maxVcores = 0;
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        cb.field("appID", TextUtils.isEmpty(appID)?"":appID);
        cb.field("nodes",nodes);
        cb.field("maxMem",maxMem);
        cb.field("maxVcores",maxVcores);
        cb.field("scan",scan);

        return cb;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public int getNodes() {
        return nodes;
    }

    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    public long getMaxMem() {
        return maxMem;
    }

    public void setMaxMem(long maxMem) {
        this.maxMem = maxMem;
    }

    public int getMaxVcores() {
        return maxVcores;
    }

    public void setMaxVcores(int maxVcores) {
        this.maxVcores = maxVcores;
    }

    public String getScan() {
        return scan;
    }

    public void setScan(String scan) {
        this.scan = scan;
    }
}
