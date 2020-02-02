package com.gbw.scanner.elasticsearch;

import java.util.List;

public class ESConfig {

    private List<String> esHosts;
    private int esPort;
    private String clusterName;

    public List<String> getEsHosts() {
        return esHosts;
    }

    public void setEsHosts(List<String> esHosts) {
        this.esHosts = esHosts;
    }

    public int getEsPort() {
        return esPort;
    }

    public void setEsPort(int esPort) {
        this.esPort = esPort;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
