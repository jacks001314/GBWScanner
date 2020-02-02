package com.gbw.scanner.plugins.scripts.web.solr;

public class SolrCoreIndex {

    private long numDocs;
    private long maxDoc;
    private long deletedDocs;
    private long indexHeapUsageBytes;
    private long version;
    private long segmentCount;
    private boolean current;
    private boolean hasDeletions;
    private String directory;
    private String segmentsFile;
    private long segmentsFileSizeInBytes;
    private String lastModified;
    private SolrCoreIndexUserData userData;
    private long sizeInBytes;
    private String size;

    public SolrCoreIndexUserData getUserData() {
        return userData;
    }

    public void setUserData(SolrCoreIndexUserData userData) {
        this.userData = userData;
    }

    public long getNumDocs() {
        return numDocs;
    }

    public void setNumDocs(long numDocs) {
        this.numDocs = numDocs;
    }

    public long getMaxDoc() {
        return maxDoc;
    }

    public void setMaxDoc(long maxDoc) {
        this.maxDoc = maxDoc;
    }

    public long getDeletedDocs() {
        return deletedDocs;
    }

    public void setDeletedDocs(long deletedDocs) {
        this.deletedDocs = deletedDocs;
    }

    public long getIndexHeapUsageBytes() {
        return indexHeapUsageBytes;
    }

    public void setIndexHeapUsageBytes(long indexHeapUsageBytes) {
        this.indexHeapUsageBytes = indexHeapUsageBytes;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getSegmentCount() {
        return segmentCount;
    }

    public void setSegmentCount(long segmentCount) {
        this.segmentCount = segmentCount;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isHasDeletions() {
        return hasDeletions;
    }

    public void setHasDeletions(boolean hasDeletions) {
        this.hasDeletions = hasDeletions;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getSegmentsFile() {
        return segmentsFile;
    }

    public void setSegmentsFile(String segmentsFile) {
        this.segmentsFile = segmentsFile;
    }

    public long getSegmentsFileSizeInBytes() {
        return segmentsFileSizeInBytes;
    }

    public void setSegmentsFileSizeInBytes(long segmentsFileSizeInBytes) {
        this.segmentsFileSizeInBytes = segmentsFileSizeInBytes;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
