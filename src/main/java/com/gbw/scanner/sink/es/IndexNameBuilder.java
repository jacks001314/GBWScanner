package com.gbw.scanner.sink.es;


public interface IndexNameBuilder {

    public String build(String indexPrefix, String docType);

}
