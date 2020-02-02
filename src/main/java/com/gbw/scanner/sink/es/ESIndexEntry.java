package com.gbw.scanner.sink.es;

import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;


public interface ESIndexEntry {

    void append(ESIndexable indexItem, String name) throws IOException;

    XContentBuilder toJson() throws IOException;

    ESIndexDB getIndexDB();

}
