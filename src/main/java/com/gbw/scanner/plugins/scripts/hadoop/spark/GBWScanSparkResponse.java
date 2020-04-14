package com.gbw.scanner.plugins.scripts.hadoop.spark;

import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public interface GBWScanSparkResponse {

    void toJson(XContentBuilder cbb) throws IOException;

}
