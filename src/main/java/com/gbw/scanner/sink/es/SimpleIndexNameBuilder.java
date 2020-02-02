package com.gbw.scanner.sink.es;


public class SimpleIndexNameBuilder implements IndexNameBuilder{

    @Override
    public String build(String indexPrefix, String docType) {

        return indexPrefix;
    }

}
