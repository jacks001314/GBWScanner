package com.gbw.scanner.plugins.scripts.weblogic.CVE20202555;

import com.gbw.scanner.plugins.scripts.GBWScanScriptCommonConfig;

public class GBWScanCVE20202555Config extends GBWScanScriptCommonConfig {

    private String payloadScriptDir;
    private String[] cmds;
    private String[] versions;
    private String defaultVersion;

    public String[] getCmds() {
        return cmds;
    }

    public void setCmds(String[] cmds) {
        this.cmds = cmds;
    }


    public String[] getVersions() {
        return versions;
    }

    public void setVersions(String[] versions) {
        this.versions = versions;
    }

    public String getDefaultVersion() {
        return defaultVersion;
    }

    public void setDefaultVersion(String defaultVersion) {
        this.defaultVersion = defaultVersion;
    }

    public String getPayloadScriptDir() {
        return payloadScriptDir;
    }

    public void setPayloadScriptDir(String payloadScriptDir) {
        this.payloadScriptDir = payloadScriptDir;
    }
}
