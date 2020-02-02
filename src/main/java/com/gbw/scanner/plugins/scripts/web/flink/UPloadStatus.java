package com.gbw.scanner.plugins.scripts.web.flink;

import com.xmap.api.utils.TextUtils;

public class UPloadStatus {

    private String filename;
    private String status;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public  String getFile(){

        if(TextUtils.isEmpty(filename))
            return null;

        return filename.substring(filename.lastIndexOf("/")+1);
    }

}
