package com.gbw.scanner.plugins.scripts.web.solr;

import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;

import java.util.List;

public class GBWScanSolrScriptConfig extends GBWScanScriptCommonConfig {

    private boolean isEncode;
    private String cmd;
    private int resultLenMax;

    private List<String> defaultCores;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public boolean isEncode() {
        return isEncode;
    }

    public void setEncode(boolean encode) {
        isEncode = encode;
    }

    public int getResultLenMax() {
        return resultLenMax;
    }

    public void setResultLenMax(int resultLenMax) {
        this.resultLenMax = resultLenMax;
    }

    public List<String> getDefaultCores() {
        return defaultCores;
    }

    public void setDefaultCores(List<String> defaultCores) {
        this.defaultCores = defaultCores;
    }
}
