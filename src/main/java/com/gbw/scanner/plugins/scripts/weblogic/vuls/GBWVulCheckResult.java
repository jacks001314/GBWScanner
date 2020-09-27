package com.gbw.scanner.plugins.scripts.weblogic.vuls;

public class GBWVulCheckResult {

    private String version;
    private String code;
    private String type;
    private String cmd;
    private String result;

    private String payloadName;

    public GBWVulCheckResult(String version,
                             String code,
                             String type,
                             String cmd,
                             String result,
                             String payloadName){

        this.version = version;
        this.code = code;
        this.type = type;
        this.cmd = cmd;
        this.result = result;
        this.payloadName = payloadName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPayloadName() {
        return payloadName;
    }

    public void setPayloadName(String payloadName) {
        this.payloadName = payloadName;
    }
}
