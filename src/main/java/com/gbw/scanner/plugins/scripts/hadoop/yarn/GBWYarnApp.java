package com.gbw.scanner.plugins.scripts.hadoop.yarn;

import com.google.gson.annotations.SerializedName;

public class GBWYarnApp {

    @SerializedName("application-id")
    private String appId;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

}
