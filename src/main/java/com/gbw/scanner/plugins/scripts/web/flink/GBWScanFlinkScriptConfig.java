package com.gbw.scanner.plugins.scripts.web.flink;

import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;

public class GBWScanFlinkScriptConfig extends GBWScanScriptCommonConfig {

    private boolean useRCE;
    private String keywords;
    private String jarFile;
    private String entryClass;
    private String submitJson;

    public boolean isUseRCE() {
        return useRCE;
    }

    public void setUseRCE(boolean useRCE) {
        this.useRCE = useRCE;
    }

    public String getJarFile() {
        return jarFile;
    }

    public void setJarFile(String jarFile) {
        this.jarFile = jarFile;
    }


    public String getEntryClass() {
        return entryClass;
    }

    public void setEntryClass(String entryClass) {
        this.entryClass = entryClass;
    }

    public String getSubmitJson() {
        return submitJson;
    }

    public void setSubmitJson(String submitJson) {
        this.submitJson = submitJson;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
