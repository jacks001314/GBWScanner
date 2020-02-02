package com.gbw.scanner.plugins.scripts.web.solr;

import com.xmap.api.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolrCoreAdmin {

    private ResponseHeader responseHeader;
    private Map<String,SolrCore> status;

    public List<String> getCoreNames(){

        List<String> res = new ArrayList<>();

        if(status!=null){

            status.keySet().forEach(e->res.add(e));
        }

        return res;
    }

    public boolean isOK(){

        return responseHeader.getStatus()==0;
    }

    public boolean isLinux(){

        for(Map.Entry<String,SolrCore> entry:status.entrySet()){

            SolrCore solrCore = entry.getValue();
            String insDir = solrCore.getInstanceDir();
            String dataDir = solrCore.getDataDir();

            if(!TextUtils.isEmpty(insDir)){

                if(insDir.startsWith("/"))
                    return true;
            }
            if(!TextUtils.isEmpty(dataDir)){
                if(dataDir.startsWith("/"))
                    return true;
            }
        }
        return false;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public Map<String, SolrCore> getStatus() {
        return status;
    }

    public void setStatus(Map<String, SolrCore> status) {
        this.status = status;
    }

    private class ResponseHeader {
        private int status;
        private long QTime;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getQTime() {
            return QTime;
        }

        public void setQTime(long QTime) {
            this.QTime = QTime;
        }

    }

}
