package com.gbw.scanner.plugins.scripts.hadoop.spark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class GBWSparkRestResponse implements GBWScanSparkResponse{

    private String action;
    private String message;
    private String serverSparkVersion;
    private String submissionId;
    private boolean success;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getServerSparkVersion() {
        return serverSparkVersion;
    }

    public void setServerSparkVersion(String serverSparkVersion) {
        this.serverSparkVersion = serverSparkVersion;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static GBWSparkRestResponse fromJson(String json){
        return new Gson().fromJson(json,GBWSparkRestResponse.class);
    }

    public String toString(){
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    @Override
    public void toJson(XContentBuilder cbb) throws IOException {

        cbb.field("action",action);
        cbb.field("message",message);
        cbb.field("serverSparkVersion",serverSparkVersion);
        cbb.field("submissionId",submissionId);
        cbb.field("success",success);

    }
}
