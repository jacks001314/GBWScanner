package com.gbw.scanner.plugins.scripts.web.solr.velocity;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;
import com.gbw.scanner.plugins.scripts.GBWScanScriptPlugin;
import com.gbw.scanner.plugins.scripts.GBWScanScriptResult;
import com.gbw.scanner.plugins.scripts.web.solr.SolrCoreAdmin;
import com.xmap.api.utils.TextUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GBWScanSolrVelocityScriptResult extends GBWScanScriptResult {

    private static final String windows = "$ex.waitFor()";
    private String system = "";
    private String result = "";
    private String core = "";
    private List<String> cores;

    public GBWScanSolrVelocityScriptResult(GBWScanScriptCommonConfig scanScriptConfig, Host host, String result, SolrCoreAdmin solrCoreAdmin,String core){

        super(scanScriptConfig,host, GBWScanScriptPlugin.solrVelScan);

        this.result = result;
        this.system = "linux";

        if((!TextUtils.isEmpty(result)&&result.contains(windows))||!solrCoreAdmin.isLinux())
            this.system = "windows";

        cores = new ArrayList<>();
        cores.addAll(solrCoreAdmin.getCoreNames());
        this.core = TextUtils.isEmpty(core)?"":core;
    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        super.makeDetails(cb);

        cb.field("system",system);
        cb.field("result",result);
        cb.field("core",core);
        cb.field("cores",cores);


        return cb;
    }


    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }


    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public List<String> getCores() {
        return cores;
    }

    public void setCores(List<String> cores) {
        this.cores = cores;
    }
}
