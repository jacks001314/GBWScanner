package com.gbw.scanner.plugins.scripts.web.solr;

public class SolrCore {

    private String name;
    private String instanceDir;
    private String dataDir;
    private String config;
    private String schema;
    private String startTime;
    private long uptime;

    private SolrCoreIndex index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstanceDir() {
        return instanceDir;
    }

    public void setInstanceDir(String instanceDir) {
        this.instanceDir = instanceDir;
    }

    public String getDataDir() {
        return dataDir;
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public SolrCoreIndex getIndex() {
        return index;
    }

    public void setIndex(SolrCoreIndex index) {
        this.index = index;
    }
}
