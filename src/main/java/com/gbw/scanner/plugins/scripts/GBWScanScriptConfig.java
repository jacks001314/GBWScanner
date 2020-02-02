package com.gbw.scanner.plugins.scripts;

import com.gbw.scanner.plugins.scripts.web.flink.GBWScanFlinkScriptConfig;
import com.gbw.scanner.plugins.scripts.web.solr.GBWScanSolrScriptConfig;
import com.gbw.scanner.plugins.scripts.windows.rdp.bluekeep.GBWScanBluekeepScriptConfig;
import com.gbw.scanner.plugins.scripts.windows.smb.MS17010.GBWScanSMBMS17010ScriptConfig;

public class GBWScanScriptConfig extends GBWScanScriptCommonConfig {

    private int threads;

    private GBWScanSolrScriptConfig solrVelocityScriptConfig;
    private GBWScanSolrScriptConfig solrDataImportScriptConfig;
    private GBWScanFlinkScriptConfig flinkScriptConfig;

    private GBWScanSMBMS17010ScriptConfig  ms17010ScriptConfig;
    private GBWScanBluekeepScriptConfig bluekeepScriptConfig;



    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public GBWScanSMBMS17010ScriptConfig getMs17010ScriptConfig() {
        return ms17010ScriptConfig;
    }

    public void setMs17010ScriptConfig(GBWScanSMBMS17010ScriptConfig ms17010ScriptConfig) {
        this.ms17010ScriptConfig = ms17010ScriptConfig;
    }

    public GBWScanBluekeepScriptConfig getBluekeepScriptConfig() {
        return bluekeepScriptConfig;
    }

    public void setBluekeepScriptConfig(GBWScanBluekeepScriptConfig bluekeepScriptConfig) {
        this.bluekeepScriptConfig = bluekeepScriptConfig;
    }

    public GBWScanFlinkScriptConfig getFlinkScriptConfig() {
        return flinkScriptConfig;
    }

    public void setFlinkScriptConfig(GBWScanFlinkScriptConfig flinkScriptConfig) {
        this.flinkScriptConfig = flinkScriptConfig;
    }


    public GBWScanSolrScriptConfig getSolrVelocityScriptConfig() {
        return solrVelocityScriptConfig;
    }

    public void setSolrVelocityScriptConfig(GBWScanSolrScriptConfig solrVelocityScriptConfig) {
        this.solrVelocityScriptConfig = solrVelocityScriptConfig;
    }

    public GBWScanSolrScriptConfig getSolrDataImportScriptConfig() {
        return solrDataImportScriptConfig;
    }

    public void setSolrDataImportScriptConfig(GBWScanSolrScriptConfig solrDataImportScriptConfig) {
        this.solrDataImportScriptConfig = solrDataImportScriptConfig;
    }
}
