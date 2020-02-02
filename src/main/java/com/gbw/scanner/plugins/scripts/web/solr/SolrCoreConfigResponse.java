package com.gbw.scanner.plugins.scripts.web.solr;

public class SolrCoreConfigResponse {

    /***
     * {
     *   "responseHeader":{
     *     "status":0,
     *     "QTime":25},
     *   "errorMessages":[{
     *       "update-queryresponsewriter":{
     *         "startup":"lazy",
     *         "name":"velocity",
     *         "class":"solr.VelocityResponseWriter",
     *         "template.base.dir":"",
     *         "solr.resource.loader.enabled":"true",
     *         "params.resource.loader.enabled":"true"},
     *       "errorMessages":["Error loading class 'solr.VelocityResponseWriter'"]}],
     *   "WARNING":"This response format is experimental.  It is likely to change in the future."}
     *
     * */

    private ResponseHeader responseHeader;
    private String WARNING;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public void setWARNING(String WARNING) {
        this.WARNING = WARNING;
    }

    public String getWARNING() {
        return WARNING;
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
