package com.gbw.scanner.plugins.scripts;

import com.gbw.scanner.plugins.scripts.hadoop.spark.GBWScanSparkConfig;
import com.gbw.scanner.plugins.scripts.hadoop.yarn.GBWScanYarnConfig;
import com.gbw.scanner.plugins.scripts.redis.GBWScanRedisConfig;
import com.gbw.scanner.plugins.scripts.web.flink.GBWScanFlinkScriptConfig;
import com.gbw.scanner.plugins.scripts.web.solr.GBWScanSolrScriptConfig;
import com.gbw.scanner.plugins.scripts.web.tomcat.GBWScanAJPConfig;
import com.gbw.scanner.plugins.scripts.weblogic.GBWScanWeblogicConfig;
import com.gbw.scanner.plugins.scripts.windows.rdp.bluekeep.GBWScanBluekeepScriptConfig;
import com.gbw.scanner.plugins.scripts.windows.smb.MS17010.GBWScanSMBMS17010ScriptConfig;

public class GBWScanScriptConfig extends GBWScanScriptCommonConfig {

    private int threads;

    private GBWScanSolrScriptConfig solrVelocityScriptConfig;
    private GBWScanSolrScriptConfig solrDataImportScriptConfig;
    private GBWScanFlinkScriptConfig flinkScriptConfig;
    private GBWScanAJPConfig tomcatAjpScriptConfig;

    private GBWScanSMBMS17010ScriptConfig  ms17010ScriptConfig;
    private GBWScanBluekeepScriptConfig bluekeepScriptConfig;

    private GBWScanYarnConfig scanYarnConfig;
    private GBWScanRedisConfig scanRedisConfig;
    private GBWScanSparkConfig scanSparkConfig;
    private GBWScanWeblogicConfig scanWeblogicConfig;

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

    public GBWScanAJPConfig getTomcatAjpScriptConfig() {
        return tomcatAjpScriptConfig;
    }

     public void setTomcatAjpScriptConfig(GBWScanAJPConfig tomcatAjpScriptConfig){

        this.tomcatAjpScriptConfig = tomcatAjpScriptConfig;
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

    public GBWScanYarnConfig getScanYarnConfig() {
        return scanYarnConfig;
    }

    public void setScanYarnConfig(GBWScanYarnConfig scanYarnConfig) {
        this.scanYarnConfig = scanYarnConfig;
    }

    public GBWScanRedisConfig getScanRedisConfig() {
        return scanRedisConfig;
    }

    public void setScanRedisConfig(GBWScanRedisConfig scanRedisConfig) {
        this.scanRedisConfig = scanRedisConfig;
    }


    public GBWScanSparkConfig getScanSparkConfig() {
        return scanSparkConfig;
    }

    public void setScanSparkConfig(GBWScanSparkConfig scanSparkConfig) {
        this.scanSparkConfig = scanSparkConfig;
    }

    public GBWScanWeblogicConfig getScanWeblogicConfig() {
        return scanWeblogicConfig;
    }

    public void setScanWeblogicConfig(GBWScanWeblogicConfig scanWeblogicConfig) {
        this.scanWeblogicConfig = scanWeblogicConfig;
    }
}
