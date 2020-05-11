package com.gbw.scanner.plugins.scripts.web.solr.dataimport;

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

public class GBWScanSolrDataImportScriptResult extends GBWScanScriptResult {

    private String result;
    private String core;

    private List<String> cores;

    public GBWScanSolrDataImportScriptResult(GBWScanScriptCommonConfig scanScriptConfig, Host host, String result, SolrCoreAdmin solrCoreAdmin,String core){

        super(scanScriptConfig,host, GBWScanScriptPlugin.solrDataImportScan);

        this.result = TextUtils.isEmpty(result)?"":result;

        cores = new ArrayList<>();
        cores.addAll(solrCoreAdmin.getCoreNames());
        this.core = TextUtils.isEmpty(core)?"":core;

    }

    @Override
    public XContentBuilder makeDetails(XContentBuilder cb) throws IOException {

        super.makeDetails(cb);
        cb.field("result",result);
        cb.field("core",core);
        cb.field("cores",cores);

        return cb;
    }


    public List<String> getCores() {
        return cores;
    }

    public void setCores(List<String> cores) {
        this.cores = cores;
    }
}
